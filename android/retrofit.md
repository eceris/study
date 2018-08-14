# [Retrofit](http://square.github.io/retrofit/)

## Introduction
Retrofit<sup>android http client</sup>은 HTTP API에 대한 요청을 Java Interface로 변경해준다. 실제로 여러가지 역할을 해주는 것 같은데, [Documents](http://square.github.io/retrofit/2.x/retrofit/)에 보면 다양한 api가 있다.

## 사용방법
annotation과 interface로 선언하고
```java
public interface GitHubService {
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
}
```
Retrofit이 GitHubService를 구현하여 동작한다.
```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```
그리고 각각의 GitHubService에서 생성된 **call** 은 원격지에 HTTP 요청(sync or async)을 한다.
```java
Call<List<Repo>> repos = service.listRepos("octocat");
```

## 적용한 내용
안드로이드에서 원격지의 api를 통해 데이터를 조회하는 것을 마치 로컬 repository에서 데이터를 조회하는 것 처럼 사용하고 싶었다. 만약 데이터를 조회하는 도중에 생기는 인프라적인 문제들(401, 500 등등..)은 ApiRepoistory에서 직접 관리하는 것이 목표이고 필요에 따라 그때그때 구현해서 사용하자.
```java
ApiRepository(final Context context) {
    this.context = context;
    this.sharedPreference = new GOSharedPreference(context);
    this.retrofit = new Retrofit.Builder()
            .baseUrl(ServerModel.getDefaultServerURL(this.context))
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .client(new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .headers(assembleApiHeaders())
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            LogWrite.i(getClass().getName(), "Authenticating for response: " + response);
                            if (System.currentTimeMillis() - latestAuthenticatedTime < 3000) {
                                // 3초 이전에 인증된 사용자라면 그냥 pass
                                throw new IOException("Canceled");
                            }
                            if (retryCount.get() > 3) {
                                // 3번 이상 호출 했을 경우 시도 중지
                                LogWrite.i(getClass().getName(), "authenticate call has benn ignored. count : " + retryCount);
                                retryCount.set(0);
                                throw new IOException("Canceled");
                            }
                            refreshSession(); // 401일 경우 세션을 새로고침
                            return response.request().newBuilder().headers(assembleApiHeaders()).build();
                        }
                    })
                    .build());
    }

// 서비스를 dynamic하게 등록하여 여러 클래스에서 참조하도록 한다.
public <T> T getService(Class<T> clazz) { 
    //singleton으로 관리할 경우 DefaultServerURL 이 변경된 경우 대응이 불가하므로 빌더에서 제외하여 세팅
    String baseUrl = ServerModel.getDefaultServerURL(this.context);
    return retrofit.baseUrl(baseUrl).build().create(clazz);
}

```

ApiRepository를 TodayController에서 사용했다.
```java
public class TodayController {

    private Context mContext;
    private DuplicateRequestPreventedApiExecutor apiExecutor; //중복 호출 방지를 위한 wrapper
    private TodayService todayService;

    public TodayController(Context context, Handler handler) {
        this.mContext = context;
        this.apiExecutor = DuplicateRequestPreventedApiExecutor.getInstance();
        this.todayService = ApiRepository.create(context).getService(TodayService.class);
    }

    public void getSurvey() {
        apiExecutor.execute(ApiUrl.TODAY_SURVEY_URL, todayService.getSurvey(0, 5, "id", "desc"), new Callback<SurveyTO>() {
            @Override
            public void onResponse(Call<SurveyTO> call, Response<SurveyTO> response) {
                SurveyTO resultData = response.body();
                resultData.setType(CardViewAdapter.TODAY_SUCCESS);
                mHandler.sendMessage(mHandler.obtainMessage(TodayFragment.WHAT_SURVEY_DATA, resultData));
            }

            @Override
            public void onFailure(Call<SurveyTO> call, Throwable t) {
                LogWrite.d("TodayController", "failed to get survey : " + t.getCause());
                SurveyTO resultData = new SurveyTO();
                resultData.setType(CardViewAdapter.TODAY_FAIL);
                mHandler.sendMessage(mHandler.obtainMessage(TodayFragment.WHAT_SURVEY_DATA, resultData));
            }
        });
    }
}
```

심플하게는 todayService.getSurvey(0, 5, "id", "desc") 호출 후 **callback을 체이닝**하여 구현할 수 있지만, 해당 서비스의 문제(todayController에서는 동일한 데이터를 매번 새로고침)를 해결 하기 위해 중복 호출은 무시하도록 DuplicateRequestPreventedApiExecutor로 wrapping하여 사용하였다.