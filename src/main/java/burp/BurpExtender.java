package burp;

import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class BurpExtender implements IBurpExtender,IHttpListener,IMessageEditorTabFactory{
    public static  PrintWriter stdout;
    public static IBurpExtenderCallbacks callbacks;
    public static IExtensionHelpers helpers;
    public static String PluginName = "PluginName:Unexpected information";
    public static String Author = "Author:xiaowei";
    public static String Team = "Team:Timeline Sec";


    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        // 设置插件名字
        callbacks.setExtensionName("Unexpected information v1.8");
        this.callbacks = callbacks;
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.helpers = callbacks.getHelpers();

        // 注册
        callbacks.registerMessageEditorTabFactory(this);
        callbacks.registerHttpListener(this);

        // 输出插件信息
        BurpExtender.stdout.println(PluginName);
        BurpExtender.stdout.println(Author);
        BurpExtender.stdout.println(Team);

    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        if (messageInfo == null){
            return;
        }
        if (messageInfo.getRequest() == null){
            return;
        }
        if (messageInfo.getResponse() == null){
            return;
        }
        // 將請求包的頭部和内容分開
        byte[] request = messageInfo.getRequest();
        IRequestInfo ana_request = helpers.analyzeRequest(request);
        byte[] byte_request = Arrays.copyOfRange(request, ana_request.getBodyOffset(), request.length);

        // 未使用请求包头
//        byte[] byte_request_head = Arrays.copyOfRange(request, 0, ana_request.getBodyOffset());

        // 將返回包的頭部和内容分開
        byte[] response = messageInfo.getResponse();

        // 包的頭部和内容(未使用)
//        IResponseInfo ana_response = helpers.analyzeResponse(response);
//        byte[] byte_response = Arrays.copyOfRange(response, ana_response.getBodyOffset(), response.length);
//        byte[] byte_response_head = Arrays.copyOfRange(response, 0, ana_response.getBodyOffset());

        // 设置高亮
//        if (Re.IP(new String(response)).length() != 0 || Re.IP(new String(byte_request)).length() != 0){
//            messageInfo.setHighlight("yellow");
//        }
        if (Re.Email(new String(request)).length() != 0 || Re.Email(new String(response)).length() != 0){
            messageInfo.setHighlight("yellow");
        }
        if (Re.IP(new String(response)).length() != 0 || Re.IP(new String(byte_request)).length() != 0){
            if (Re.in_ip(new String(response)) ||Re.in_ip(new String(byte_request))){
                messageInfo.setHighlight("red");
            }
        }
//        if (Re.Password((new String(byte_response))).length() !=0){
//            messageInfo.setHighlight("yellow");
//        }
//        if (Re.Address((new String(byte_response))).length() != 0){
//            messageInfo.setHighlight("orange");
//        }
        if (Re.Phone(new String(request)).length() != 0 || Re.Phone(new String(response)).length() != 0){
            messageInfo.setHighlight("green");
        }
        if (Re.IdCard(new String(request)).length() != 0 || Re.IdCard(new String(response)).length() != 0){
            messageInfo.setHighlight("green");
        }
    }

    @Override
    public burp.IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
        return new IMessageEditorTab();
    }

    class IMessageEditorTab implements burp.IMessageEditorTab {

        // 文本
        private ITextEditor iTextEditor = callbacks.createTextEditor();

        @Override
        public String getTabCaption() {
            return "Unexpected information";
        }

        @Override
        public Component getUiComponent() {
            return iTextEditor.getComponent();
        }

        @Override
        public boolean isEnabled(byte[] content, boolean isRequest) {
            if (isRequest == true){ // 请求包匹配
                // 提取请求包body内容
                IRequestInfo request = helpers.analyzeRequest(content);
                byte[] request_body = Arrays.copyOfRange(content, request.getBodyOffset(), content.length);

                if (Re.Phone(new String(content)).length() != 0 || Re.IdCard(new String(content)).length() != 0
                        || Re.IP(new String(request_body)).length() != 0 || Re.Email(new String(request_body)).length() != 0){
                    return true;
                }
            }else { // 返回包匹配
                //返回包内容 (js不匹配head)
                IResponseInfo response = helpers.analyzeResponse(content);
                List<String> headers = response.getHeaders();
                byte[] response_body = Arrays.copyOfRange(content, response.getBodyOffset(), content.length);

                if (Re.Phone(new String(content)).length() != 0 || Re.IdCard(new String(content)).length() != 0
                        || Re.Email(new String(content)).length() != 0 || Re.IP(new String(content)).length() != 0
                        || Re.Password(new String(content)).length() !=0 || Re.js(headers.toString(),response_body)/** || Re.Address(new String(body)).length() !=0 **/){
                    return true;
                }
            }
            return false;
        }

        @Override
        public void setMessage(byte[] content, boolean isRequest) {

            //取头部信息以及body信息 给js判断使用
            IResponseInfo response = helpers.analyzeResponse(content);
            List<String> headers = response.getHeaders();
            byte[] response_body = Arrays.copyOfRange(content, response.getBodyOffset(), content.length);

            // 引用规则匹配
            String Text = "";
            String phone = Re.Phone(new String(content));
            String id = Re.IdCard(new String(content));
            String ip = Re.IP(new String(content));
            String email = Re.Email(new String(content));
//            String address = Re.Address(new String(content));
            String password = Re.Password(new String(content));

            // 设置文本
            if (phone.length() != 0) {
                Text += "Exists phone information: " +phone+ '\n';
            }
            if (id.length() != 0){
                Text += "Exists IdCard information: " +id+ '\n'+ '\n';
            }
            if (ip.length() != 0){
                Text += "Exists ip information: " +ip+ '\n'+ '\n';
            }
            if (email.length() != 0){
                Text += "Exists email information: " +email+ '\n'+ '\n';
            }
            if (password.length() !=0){
                Text += "Exists Special Field ("+password+")"+'\n'+ '\n';
            }
            if(Re.js(headers.toString(),content)){
                String path = Re.Path(new String(response_body));
                Text += "Interface information: "+'\n'+path+ '\n';
            }

            iTextEditor.setText(helpers.stringToBytes(Text));

            return;
        }

        @Override
        public byte[] getMessage() {
            return iTextEditor.getText();
        }

        @Override
        public boolean isModified() {
            return false;
        }

        @Override
        public byte[] getSelectedData() {
            return iTextEditor.getSelectedText();
        }
    }
}

