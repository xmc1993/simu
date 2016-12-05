package socket;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by xiaofengxu on 16/11/22.
 */
public class ProtoTest {
    @Protobuf(fieldType = FieldType.STRING, order=1, required = true)
    public String id;
    @Protobuf(fieldType = FieldType.STRING, order=2, required = true)
    public String time;
}
