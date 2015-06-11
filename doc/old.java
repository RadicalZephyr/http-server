try (ServerSocket listenSocket = new ServerSocket(portNumber)) {
    System.err.println("Listening for clients...");

    while (true) {
        try (Socket socket = listenSocket.accept();
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                 new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(
                 new InputStreamReader(System.in))) {
            System.err.println("Connected to client.");

            String request = in.readLine();
            String[] params = request.split(" ");

            assert(params.length == 3);

            String method = params[0];
            String path = params[1];
            String protocolVersion = params[2];

            System.out.format("Client requested to %s file %s over %s.\n",
                              method, path, protocolVersion);
            out.format("%s 200 OK\r\n", protocolVersion);
        }
    }
}
