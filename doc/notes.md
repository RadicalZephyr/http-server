In order to properly unit test my code, I'll need to isolate myself
from the Socket and ServerSocket classes.

They don't implement any useful interfaces, so I'll need to
encapsulate them with my own interface and adapter class.

The adapter can also isolate me from the ceremony of creating the
Buffered Readers and Print Writer's that are needed.

I think it's okay to have the adapter return the reader and writer
since those are interfaces... maybe?

Maybe it's not though. Maybe it's better to have the adapter
encapsulate all the reading aspects...

an HttpRequest and HttpResponse

The Request and Response come in pairs, backed by the same Socket.

But that doesn't help me with isolating from the socket. I do actually
need just a SocketAdapter interface that I can mock.

HttpSocket might be a reasonable interface, not sure what it needs.
