# 3장 컴포즈 핵심 원칙 자세히 알아보기

[3장 내용 정리](https://hwajin-jung.notion.site/3-1b8e63cb443b4ea08bd243989a18ab97)


### 3장 실습 실행화면
| ShortColoredTextDemo | BoxWithConstraints |
| ------ |--------|
|![img](https://user-images.githubusercontent.com/61824695/218643198-55e48c9a-28e0-4224-8c25-9767974b0953.png)     |![img](https://user-images.githubusercontent.com/61824695/218643443-d602ebb2-ef29-4aaa-8f27-7e815d24274c.png)|


| orderDemo | drawCrossLine() |
| ------ |--------|
|![img](https://user-images.githubusercontent.com/61824695/218643877-6a5ade01-a6b8-4eae-a3e8-21657f53a2fc.png)     |![img](https://user-images.githubusercontent.com/61824695/218643923-bf003b4f-58ab-475d-9a84-a86b8ba0df04.png)


### 3장 스터디 내용
- 105p 변경자 동작 이해
- `modifier 매개변수는 첫 번째 널이 가능한 매개변수가 돼야 하므로 후행 람다 표현식을 제외한 필요로 하는 모든 매개변수의 맨 뒤에 위치해야 한다.`
    - nullable한 parameter가 먼저 오게 되면, Composable 함수를 사용할 때 굳이 정의하지 않아도 되는 parameter값의 초기화를 강제받게 된다.
    - Composable 함수에서 정의한 Modifier를 자식 Composable 함수에서 추가로 설정하기 위해 Modifier를 반드시 받는 구조이기 때문에 NonNull인 Modifier를 맨 앞으로 가져오는 것이 경제적
    - 코드에서 관련 부분 확인