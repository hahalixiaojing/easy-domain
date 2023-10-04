package cn.easylib.domain.visual.output.markdown;

import cn.easylib.domain.visual.DomainModelVisualInfo;
import org.junit.Test;

public class MarkdownVisualOutputTest {

    @Test
    public void outputTest(){

        MarkdownVisualOutput markDownVisualOutput = new MarkdownVisualOutput();

        String output = markDownVisualOutput.output(new DomainModelVisualInfo(null, null, null, null, null));

        System.out.println(output);
    }
}
