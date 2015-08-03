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



------

Current issue, how do I parse the request, how do I pass it into the
Protocol parser, and how do I store the request body in the Request.

This is an interesting issue because the http protocol is explicitly
character based and human readable for the header portion, but the
body type and encoding and everything is determined by the headers,
and is potentially binary (though I don't need to deal with binary
body data).

So, I think there should be no processing done to the input stream
before the Protocol gets it because that's what is responsible for the
processing. So the Protocol method will take an InputStream
now. Conversely, the protocol is what should know about looking for
the content-length header and then reading the data in.  But the data
is still potentially binary in nature then, so it should be read into
some byte based format. A list of bytebuffers sounds reasonable.
Though a list of byte[] might be easier with an inputstream.  Then
there can be middleware that handles unwrapping different content
types, and decoding them.
