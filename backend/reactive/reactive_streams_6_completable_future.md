#CompletableFuture


complete();
completeExceptionally(new RuntimeException());




CompletableFuture.supplyAsync(() -> {})
.thenApply(() -> {})
.thenApply(() -> {})
.thenApply(() -> {})
.thenAccept(() -> {});

ForkJoinPool.commonPool().shutdown();


thenApply의 리턴값으로 completable future를 받고 싶을 경우 thenCompose()로 할것.

thenCompose() : Stream의 flatMap
thenApply() : Map 
으로 생각하면 된다.
