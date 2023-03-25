package org.example;

import org.apache.http.entity.ContentType;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
import org.json.JSONArray;
import org.json.JSONObject;
import us.abstracta.jmeter.javadsl.JmeterDsl;
import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.core.DslTestPlan.TestPlanChild;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.listeners.BaseListener;
import us.abstracta.jmeter.javadsl.core.preprocessors.DslJsr223PreProcessor.PreProcessorVars;
import us.abstracta.jmeter.javadsl.core.threadgroups.BaseThreadGroup;
import us.abstracta.jmeter.javadsl.dashboard.DashboardVisualizer;


import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {
    private Random random;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        String regularUrl = "http://localhost:8080/images";
        String asyncUrl = "http://localhost:8080/images/async";
        String reactiveUrl = "http://localhost:8080/reactive/image";
        String nodeUrl = "http://localhost:3000/express/image";
        TestPlanStats testPlanStats = main.createDslTestPlanForRegular(reactiveUrl).run();
        System.out.println(testPlanStats);
    }

    public DslTestPlan createDslTestPlanForRegular(String url) {
        return JmeterDsl.testPlan(
                JmeterDsl.threadGroup().rampTo(10000, Duration.ofSeconds(1)).holdIterating(1).children(
                        JmeterDsl.httpSampler(url)
                                .method(HTTPConstants.POST)
                                .contentType(ContentType.APPLICATION_JSON)
                                .body(this::getRequestBody)
                ),
                DashboardVisualizer.dashboardVisualizer()
        );
    }

    private List<BaseThreadGroup.ThreadGroupChild> getThreadGroupChileList(String url){
        return IntStream.range(0, 2000).mapToObj(num -> JmeterDsl.httpSampler(url)
                .method(HTTPConstants.POST)
                .contentType(ContentType.APPLICATION_JSON)
                .body(this::getRequestBody)).collect(Collectors.toList());
    }

    public TestPlanChild createConsolePrinter() {
        return new BaseListener("name", SampleDataLogger.class) {
            @Override
            protected TestElement buildTestElement() {
                return new ResultCollector();
            }
        };
    }


    public String getRequestBody(PreProcessorVars preProcessorVars) {
        JSONArray imagesArray = new JSONArray(IntStream.range(0, 10).mapToObj(value -> new JSONObject(getImageDetailsAsMap(value))).toArray());
        Map<String, JSONArray> imageDetailsMap = new HashMap<>();
        imageDetailsMap.put("images", imagesArray);
        //return imagesArray.toString();
        return new JSONObject(imageDetailsMap).toString();
    }

    public Map<String, String> getImageDetailsAsMap(int index) {
        random = new Random(System.currentTimeMillis());
        long next = random.nextInt();
        Map<String, String> imageDetailsMap = new HashMap<>();
        imageDetailsMap.put("name", "cute dog - " + Math.abs(next));
        imageDetailsMap.put("width", "120");
        imageDetailsMap.put("height", "120");
        imageDetailsMap.put("format", index % 3 == 0 ? "jpg" : index % 3 == 1 ? "png" : "jpeg");
        imageDetailsMap.put("url", "https://images.examples.com/cute_dog_" + Math.abs(next));
        imageDetailsMap.put("size", "1320000");
        imageDetailsMap.put("device", index % 5 == 0 ? "DSLR" : index % 5 == 1 ? "PHONE" : index % 5 == 2 ? "DIGITAL_CAMERA" : index % 5 == 3 ? "GO_PRO" : "PIXEL");
        imageDetailsMap.put("number_of_pixels", "14400");
        imageDetailsMap.put("created_date", "2017-07-21T17:32:28Z");
        imageDetailsMap.put("last_modified_date", "2018-07-21T17:32:28Z");
        imageDetailsMap.put("captured_by", "sandun - " + Math.abs(next));
        return imageDetailsMap;
    }
}