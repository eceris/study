package kr.co.eceris.demojava11.lambda;

import reactor.core.publisher.Flux;
import reactor.util.annotation.NonNull;

public class LambdaVarExam {

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5)
                .map((@NonNull var i) -> i * 2)
//                .map(i -> i * 2)
                .subscribe(System.out::println);
    }
}
