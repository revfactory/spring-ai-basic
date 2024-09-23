# Spring AI 예제 프로젝트

이 프로젝트는 Spring AI를 활용한 다양한 AI 기능을 구현한 예제 애플리케이션입니다. 각 예제는 ChatGPT, DALL-E, 그리고 기타 AI 모델들을 사용하여 다양한 작업을 수행합니다.

## 예제1) Hello

`ChatController.java`에 구현된 간단한 채팅 기능입니다.

```java
@GetMapping("/hello")
public String chatWithTemplate(@RequestParam(name = "name") String name) {
    PromptTemplate promptTemplate = new PromptTemplate("안녕? 내 이름은 {name} 이야");
    Prompt prompt = promptTemplate.create(Map.of("name", name));
    return chatClient.prompt(prompt)
            .call()
            .content();
}
```

이 예제는 사용자의 이름을 입력받아 간단한 인사말을 생성합니다.

## 예제2) 고양이 이미지 생성

`ImageController.java`에 구현된 이미지 생성 기능입니다.

```java
@GetMapping
public String generateImage(@RequestParam String request) {
    ImageOptions options = ImageOptionsBuilder.builder()
            .withModel("dall-e-3").withWidth(1024).withHeight(1024).build();

    ImagePrompt prompt = new ImagePrompt(request, options);
    ImageResponse response = imageModel.call(prompt);
    String imageUrl = response.getResult().getOutput().getUrl();
    return "redirect:" + imageUrl;
}
```

이 예제는 사용자의 요청에 따라 DALL-E 3 모델을 사용하여 1024x1024 크기의 이미지를 생성합니다.

## 예제3) 이미지 분석

`VisionController.java`에 구현된 이미지 분석 기능입니다.

```java
@PostMapping("upload")
@ResponseBody
public ResponseEntity<Map<String, String>> vision(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("message") String message) {
    // ... (코드 생략)
    String result = chatClient.prompt()
            .user(userSpec -> userSpec
                    .text(userMessage)
                    .media(MimeType.valueOf(Objects.requireNonNull(file.getContentType())), file.getResource()))
            .call()
            .content();
    // ... (코드 생략)
}
```

이 예제는 사용자가 업로드한 이미지를 분석하고, 선택적으로 사용자가 제공한 메시지를 함께 처리합니다.

## 예제4) 자동으로 SQL 쿼리 작성해서 조회하기

`SqlController.java`에 구현된 SQL 쿼리 자동 생성 및 실행 기능입니다.

```java
@GetMapping
public SqlResponse sql(@RequestParam(name = "q") String question) throws IOException {
    // ... (코드 생략)
    String query = aiClient.prompt()
            .user(userSpec -> userSpec
                    .text(sqlPromptTemplateResource)
                    .param("question", question)
                    .param("ddl", schema)
            )
            .call()
            .content();
    // ... (코드 생략)
}
```

이 예제는 사용자의 질문을 바탕으로 SQL 쿼리를 자동으로 생성하고, 해당 쿼리를 실행하여 결과를 반환합니다.

## 예제5) Json Dummy 데이터 만들기

`DummyController.java`에 구현된 더미 데이터 생성 기능입니다.

```java
@GetMapping
public List<User> dummy() {
    return chatClient.prompt()
            .user("더미 데이터를 10개 생성해줘")
            .call()
            .entity(new ParameterizedTypeReference<>() {});
}
```

이 예제는 AI를 사용하여 10개의 더미 User 객체를 생성합니다.

## 예제6) 해커뉴스 검색기

`HackerNewsController.java`에 구현된 해커뉴스 검색 기능입니다.

```java
@PostMapping
@ResponseBody
public String getNews(@RequestParam("request") String request) {
    return chatClient.prompt()
            .system("당신은 뉴스 전문가입니다. 사용자의 질문에서 주제를 영문으로 추출하여 최신 뉴스를 제공합니다. 최종 결과물은 한글로 작성합니다.")
            .functions("getNews")
            .user(request)
            .call()
            .content();
}
```

이 예제는 사용자의 요청을 바탕으로 해커뉴스에서 관련 뉴스를 검색하고 요약하여 제공합니다.

각 예제는 Spring AI의 다양한 기능을 활용하여 구현되었으며, 실제 애플리케이션에서 AI 기능을 어떻게 통합할 수 있는지 보여줍니다.
