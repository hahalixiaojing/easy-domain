package cn.easylib.domain.visual.output.markdown;

import cn.easylib.domain.visual.DomainModelVisualInfo;
import org.junit.Test;

public class MarkDownVisualOutputTest {

    @Test
    public void outputTest(){

        MarkDownVisualOutput markDownVisualOutput = new MarkDownVisualOutput();

        String output = markDownVisualOutput.output(new DomainModelVisualInfo(null, null, null, null, null));

        System.out.println(output);
    }
}
