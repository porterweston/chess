package serviceTests;

import server.*;

public class ClearServiceTest {
    void clearApplicationTest(){
        ClearService clearService = ClearService.getInstance();
        clearService.clearApplication();
    }
}
