package com.vtaveira.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public class SocketConnectionEvents {
  public Consumer<byte[]> onDataReceived = _ -> {};
  public Consumer<IOException> onWriteDataError = _ -> {};
  public Consumer<IOException> onReadDataError = _ -> {};
  public Consumer<IOException> onSetupStreamsError = _ -> {};
}
