package automation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
    private static final Logger logger =
            LoggerFactory.getLogger(RetryAnalyzer.class);
    int counter = 0;
    int retryLimit = 1;
    
    @Override
    public boolean retry(ITestResult result) {
        
        if(result.getStatus()==2 && counter < retryLimit) {
          logger.info(result.getTestClass().getName() + "******" + result.getMethod().getMethodName());
          counter++;
          return true;
        }
        return false;
    }

}
