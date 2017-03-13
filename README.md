# gvertx
`基于guice and vertx 构建一站式的 reactive框架 `

## reset verticle
	@Instance(val = 10)
	public class RestVerticle extends RestAbstractVerticle 

## start
	@Override
	public void start() {
    	GET().route("/hello.json").with(TestController.class,"hello");
    	vertx.createHttpServer()
            .requestHandler(router::accept)
            .rxListen(port).subscribe(
                    server -> log.info(String.format("Server is now listening.  Thread:%s ", Thread.currentThread())),
                    failure -> log.info(String.format("Server could not start. Thread:%s", Thread.currentThread()), failure)
            );

    }

## run
    public static void main(String[] args) {
        new Runner().run(RestVerticle.class);
    }
