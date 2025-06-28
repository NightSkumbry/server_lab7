package controls;

import connections.PrintType;
import connections.ResponseType;
import connections.ServerRequest;
import connections.ServerResponse;
import consoles.Printer;
import util.StringWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConnectionControl {
    static class Sender extends RecursiveAction {
        private final int bufferSize = 32768; // Размер буфера для передачи данных
        private final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        private ServerResponse response;
        private InetSocketAddress clientAddress;
        private Printer printer;
        private DatagramChannel channel;

        public Sender(ServerResponse response, InetSocketAddress clientAddress, DatagramChannel channel, Printer printer) {
            this.response = response;
            this.clientAddress = clientAddress;
            this.channel = channel;
            this.printer = printer;
        }
        

        private byte[] serializeResponse(ServerResponse response) {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                response.writeExternal(objectOutputStream); // Вызываем метод writeExternal
                objectOutputStream.flush();
                return byteArrayOutputStream.toByteArray(); // Возвращаем массив байтов
            } catch (IOException e) {
                printer.printError("Ошибка при сериализации ответа: " + e.getMessage());
                return new byte[0];
            }
        }


        @Override
        protected void compute() {
            try {
                buffer.clear();
                byte[] data = serializeResponse(response);
                buffer.put(data);
                buffer.flip();
                channel.send(buffer, clientAddress);
            } catch (IOException e) {
                printer.printError("Ошибка при отправке данных: " + e.getMessage());
            }
        }
    }


    private DatagramChannel channel;
    private Selector selector;
    private final int bufferSize = 32768; // Размер буфера для передачи данных
    private final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
    private final List<InetSocketAddress> connectedUsers = new ArrayList<>(); // Список подключенных пользователей
    private Printer printer;
    private DataBaseControl dataBaseControl; // Экземпляр класса для управления авторизацией
    private ForkJoinPool forkJoinPool = new ForkJoinPool(); // Пул потоков для асинхронной отправки данных

    /**
     * Инициализирует сетевой канал для работы по протоколу UDP.
     *
     * @param port Порт, на котором сервер будет принимать данные.
     */
    public ConnectionControl(int port, Printer printer) {
        this.printer = printer;
        dataBaseControl = new DataBaseControl(printer);
        try {
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            printer.println("Сервер запущен на порту: " + port);
        } catch (IOException e) {
            printer.printError("Ошибка при инициализации канала: " + e.getMessage());
        }
    }

    /**
     * Проверяет, есть ли доступные данные для чтения.
     *
     * @return true, если данные доступны, иначе false.
     */
    public boolean isReady() {
        try {
            return selector.selectNow() > 0;
        } catch (IOException e) {
            printer.printError("Ошибка при проверке доступности данных: " + e.getMessage());
            return false;
        }
    }

    /**
     * Читает данные из канала.
     *
     * @return Принятый {@link ServerRequest}, или null, если данных нет.
     */
    public ServerRequest read() {
        try {
            int readyChannels = selector.selectNow();
            if (readyChannels == 0) return null;

            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isReadable()) {
                    buffer.clear();
                    InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
                    if (clientAddress != null) {
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);

                        
                        int clientId = connectedUsers.indexOf(clientAddress);
                        if (clientId == -1) {
                            connectedUsers.add(clientAddress);
                            clientId = connectedUsers.size() - 1;
                        }
                        else {
                            connectedUsers.set(clientId, clientAddress);
                        }
                        

                        ServerRequest request = deserializeRequest(data);
                        if (request != null) {
                            // auth handler
                            if (!request.getContent().equals("log_in")) {
                                boolean auth;
                                try {
                                    auth = dataBaseControl.authenticateUser(request.getUser().getName(), request.getUser().getPassword());
                                }
                                catch (Exception e) {
                                    printer.printError("Ошибка при аутентификации пользователя: " + e.getMessage());
                                    sendResponse(new ServerResponse(request.getClientCommandId(), ResponseType.AUTH_FAILURE, PrintType.ERROR, new StringWrapper("Ошибка при аутентификации"), clientId));
                                    return null;
                                }
                                if (!auth) {
                                    sendResponse(new ServerResponse(request.getClientCommandId(), ResponseType.AUTH_FAILURE, PrintType.ERROR, new StringWrapper("Неверный логин или пароль"), clientId));
                                    return null;
                                }
                            }

                            // command handler
                            request.setClientId(clientId);

                            printer.println("Получена команда " + request.getContent() + " от клиента " + clientId);

                            selector.selectedKeys().clear();
                            return request;
                        }
                        else {
                            String received = new String(data);
                            if (received.equals("echo")) {
                                printer.println("Получено echo от клиента " + clientId);
                                buffer.clear();
                                buffer.put("OK".getBytes());
                                buffer.flip();
                                channel.send(buffer, clientAddress);
                            }
                            else {
                                buffer.clear();
                                buffer.put("NO".getBytes());
                                buffer.flip();
                                channel.send(buffer, clientAddress);
                            }
                            selector.selectedKeys().clear();
                            return null;
                        }
                    } else {selector.selectedKeys().clear(); return null; }
                }
            }
            selector.selectedKeys().clear();
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
        }
        return null; // Если данных нет
    }

    /**
     * Отправляет ответ клиенту.
     *
     * @param response {@link ServerResponse} для отправки.
     */
    public void sendResponse(ServerResponse response) {

            int clientId = response.getClientId();
            if (clientId >= 0 && clientId < connectedUsers.size()) {
                InetSocketAddress clientAddress = connectedUsers.get(clientId);
                forkJoinPool.submit(new Sender(response, clientAddress, channel, printer));                
            }
    }

    /**
     * Закрывает сетевой канал.
     */
    public void close() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            printer.printError("Ошибка при закрытии канала: " + e.getMessage());
        }
    }

    

    private ServerRequest deserializeRequest(byte[] data) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            ServerRequest request = new ServerRequest();
            request.readExternal(objectInputStream); // Вызываем метод readExternal
            return request;
        } catch (IOException | ClassNotFoundException e) {
            // printer.printError("Ошибка при десериализации запроса: " + e.getMessage());
            return null;
        }
    }
}
