package cn.easylib.domain.visual.output.markdown;

import cn.easylib.domain.visual.entity.EnumInfoDescriptor;
import cn.easylib.domain.visual.entity.EnumValue;
import cn.easylib.domain.visual.entity.IEnumValueVisualOutput;
import org.apache.commons.lang3.SystemUtils;

import java.util.List;

public class MarkDownEnumValueVisualOutput implements IEnumValueVisualOutput {
    @Override
    public String output(List<EnumInfoDescriptor> enumInfoDescriptorList) {

        StringBuilder stringBuilder = new StringBuilder();
        enumInfoDescriptorList.forEach(e -> {
            String s = this.buildEnumValues(e.getName(), e.getValueList());
            stringBuilder.append(s);
        });

        return stringBuilder.toString();
    }

    private String buildEnumValues(String name, List<EnumValue> enumValues) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("### *").append(name);
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append("|名称|值|描述|");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append("|-|-|-|");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        enumValues.forEach(e -> {

            stringBuilder.append("|");
            stringBuilder.append(e.getName());
            stringBuilder.append("|");
            stringBuilder.append(e.getValue());
            stringBuilder.append("|");
            stringBuilder.append(e.getDescription());
            stringBuilder.append("|");
            stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        });
        return stringBuilder.toString();

    }
}
