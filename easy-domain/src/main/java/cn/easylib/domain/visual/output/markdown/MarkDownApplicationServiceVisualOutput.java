package cn.easylib.domain.visual.output.markdown;

import cn.easylib.domain.visual.application.ApplicationDescriptor;
import cn.easylib.domain.visual.application.IApplicationServiceVisualOutput;
import org.apache.commons.lang3.SystemUtils;

import java.util.List;

public class MarkDownApplicationServiceVisualOutput implements IApplicationServiceVisualOutput {
    @Override
    public String output(List<ApplicationDescriptor> applicationDescriptorList) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|类名|方法|类型|描述|");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append("|-|-|-|-|");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);


        applicationDescriptorList.forEach(a->{

            String s = this.buildRow(a);
            stringBuilder.append(s);
            stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        });



        return stringBuilder.toString();
    }

    private String buildRow(ApplicationDescriptor applicationDescriptor) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|");
        stringBuilder.append(applicationDescriptor.getClsName());
        stringBuilder.append("|");
        stringBuilder.append(applicationDescriptor.getMethodName());
        stringBuilder.append("|");
        stringBuilder.append(applicationDescriptor.getType());
        stringBuilder.append("|");
        stringBuilder.append(applicationDescriptor.getApplicationServiceDescription());
        stringBuilder.append("|");

        return stringBuilder.toString();
    }
}
