@SpringBootApplication
public class TranslationApp {
    public static void main(String[] args) {
        SpringApplication.run(TranslationApp.class, args);
    }

    @RestController
    @RequestMapping("/translate")
    public class TranslationController {
        private final TranslationService translationService;

        public TranslationController(TranslationService translationService) {
            this.translationService = translationService;
        }

        @PostMapping
        public TranslationResponse translateText(@RequestBody TranslationRequest request) {
            String translatedText = translationService.translateText(
                request.getText(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
            );
            return new TranslationResponse(translatedText);
        }
    }

    @Service
    public class TranslationService {
        private final Translator translator;

        public TranslationService() {
            TranslationOptions options = TranslationOptions.newBuilder()
                .setTargetLanguage("en")
                .build();
            this.translator = TranslationServiceClient.create(options);
        }

        public String translateText(String text, String sourceLanguage, String targetLanguage) {
            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setcontents(text)
                .setSourceLanguageCode(sourceLanguage)
                .setTargetLanguageCode(targetLanguage)
                .build();

            TranslateTextResponse response = translator.translateText(request);
            return response.getTranslations(0).getTranslatedText();
        }
    }

    @Data
    @AllArgsConstructor
    public static class TranslationRequest {
        private String text;
        private String sourceLanguage;
        private String targetLanguage;
    }

    @Data
    @AllArgsConstructor
    public static class TranslationResponse {
        private String translatedText;
    }
}
