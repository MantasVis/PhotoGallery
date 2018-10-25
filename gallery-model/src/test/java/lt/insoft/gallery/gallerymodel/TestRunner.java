package lt.insoft.gallery.gallerymodel;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DatabaseTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println("\n");
            System.out.println("-----------------------FAILURE-----------------------: " + failure.toString() + "\n");
        }
        System.out.println("\n");
        System.out.println("-----------------------SUCCESS-----------------------: " + result.wasSuccessful() + "\n");
    }
}
