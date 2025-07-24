package com.vtaveira.client.network;


import com.vtaveira.builds.java.WrapperMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NetworkClient {
  private final AtomicBoolean connected = new AtomicBoolean(false);
  private final BlockingQueue<WrapperMessage> messageQueue = new LinkedBlockingQueue<>();
  private Socket clientSocket;
  private InputStream inputStream;
  private OutputStream outputStream;
  private Thread readerThread;
  private Thread writerThread;

  public boolean connect(String host, int port) {
    try {
      this.clientSocket = new Socket(host, port);
      this.inputStream = this.clientSocket.getInputStream();
      this.outputStream = this.clientSocket.getOutputStream();
      this.connected.set(true);
      this.startMessageHandling();
      log.info("connected to {}:{}", host, port);
      return true;
    } catch (IOException e) {
      log.error("failed to connect to [{}:{}]: {}", host, port, e.getCause().getMessage());
      return false;
    }
  }

  private void startMessageHandling() {
    this.readerThread = this.startReader();
    this.writerThread = this.startWriter();
  }

  private Thread startReader() {
    return Thread.ofVirtual()
        .name("NetworkReader")
        .start(() -> {
          while (this.connected.get() && !Thread.currentThread().isInterrupted()) {
            try {
              var len = this.inputStream.read();
              var bytes = this.inputStream.readNBytes(len);

              var message = WrapperMessage.parseFrom(bytes);
              log.debug("received message, size: {} {}", len, message);
              ProtobufHandler.getInstance().handleIncomingMessage(message);
            } catch (IOException e) {
              if (this.connected.get()) {
                log.error("failed to read message from server:", e);
                this.disconnect();
              }
              return;
            }
          }
        });
  }

  private Thread startWriter() {
    return Thread.ofVirtual()
        .name("NetworkWriter")
        .start(() -> {
          while (this.connected.get() && !Thread.currentThread().isInterrupted()) {
            try {
              var payload = this.messageQueue.take();
              var len = payload.getSerializedSize();
              var bytes = payload.toByteArray();
              log.debug("sending message: {}, size: {}", payload, len);
              this.outputStream.write(len);
              this.outputStream.write(bytes);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              log.warn("writer thread interrupted, stopping...", e);
              return;
            } catch (IOException e) {
              log.error("error writing message: {}", e.getMessage());
              this.disconnect();
              return;
            }
          }
        });
  }

  public void sendMessage(WrapperMessage message) {
    if (this.connected.get()) {
      var success = this.messageQueue.offer(message);
      log.debug("register message to send: {}{}", message, success);
    }
  }

  public void disconnect() {
    this.connected.set(false);
    if (this.readerThread != null) {
      this.readerThread.interrupt();
    }
    if (this.writerThread != null) {
      this.writerThread.interrupt();
    }

    try {
      if (this.clientSocket != null && !this.clientSocket.isClosed()) {
        this.clientSocket.close();
      }
    } catch (IOException e) {
      log.error("failed to close socket: {}", e.getMessage());
    }
  }

  public boolean isConnected() {
    return this.connected.get() && this.clientSocket != null && !this.clientSocket.isClosed();
  }
}
