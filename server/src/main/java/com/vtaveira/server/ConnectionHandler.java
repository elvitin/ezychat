package com.vtaveira.server;

import io.vavr.control.Try;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


@Slf4j
public abstract class ConnectionHandler implements Runnable {
  @Getter public final String ipPort;

  @Getter
  @Accessors(fluent = true)
  private final SocketConnectionEvents events;
  private final Socket socket;
  private final Thread thread;
  @Setter
  @Getter
  private String identity;
  private DataInputStream inFromClient;
  private DataOutputStream outToClient;

  public ConnectionHandler(Socket socket, SocketConnectionEvents events) {
    this.events = events;
    this.socket = socket;
    this.ipPort = Utils.getClientIPPort(socket);
    this.thread = new Thread(this);
    this.thread.setDaemon(true);
    this.thread.setName(this.ipPort);
  }

  @Override
  public void run() {
    log.info("client connected: {}", this.ipPort);
    Try.run(() -> {
      while (this.socket.isConnected()) {
        var len = this.inFromClient.readInt();
        byte[] bytes = inFromClient.readNBytes(len);
        log.debug("received data from client ip [{}] size [{}]", this.ipPort, bytes.length);
        this.events.onDataReceived.accept(bytes);
      }
    }).onFailure(e -> {
      this.closeResources();
      this.events.onReadDataError.accept((IOException) e);
      log.error("error reading data from client ip [{}]: {}", this.ipPort, e.getLocalizedMessage());
    });
  }

  public void start() {
    Try.run(() -> {
      this.inFromClient = new DataInputStream(socket.getInputStream());
      this.outToClient = new DataOutputStream(socket.getOutputStream());
    }).onSuccess(_ -> this.thread.start()).onFailure(e -> events.onSetupStreamsError.accept((IOException) e));
  }


  public void writeData(byte[] data) {
    Try.run(() -> {
      this.outToClient.writeInt(data.length);
      this.outToClient.write(data);
      log.debug("data sent to client ip [{}] size [{}]", this.ipPort, data.length);
    }).onFailure(e -> {
      this.closeResources();
      this.events.onWriteDataError.accept((IOException) e);
      log.error("error sending data to client ip [{}]: {}", this.ipPort, e.getLocalizedMessage());
    });
  }

  private void closeResources() {
    Try.run(this.outToClient::close);
    Try.run(this.inFromClient::close);
    Try.run(this.socket::close);
  }
}
