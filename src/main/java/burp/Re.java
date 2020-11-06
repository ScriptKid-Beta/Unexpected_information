package burp;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Re {
    // 手机号匹配
    public static String Phone(String str){
        ArrayList<String> phones = new ArrayList<>();
        String is_number = "\\b\\d{11,}";
        Matcher matcher = Pattern.compile(is_number).matcher(str);
        while (matcher.find())
        {
            if (matcher.group().length() == 11) {
                String is_phone = "^(13[0-9]|14[5|7]|15[^4]|17[6-9]|18[0-9])\\d{8}$";
                Matcher matcher2 = Pattern.compile(is_phone).matcher(matcher.group());
                    while (matcher2.find()) {
                        phones.add(matcher2.group());
                }
            }
        }
        // 去重
        List<String> phones_rd = Re.removeDuplicate(phones);
        return StringUtils.strip(phones_rd.toString(),"[]");
    }
    // 匹配地址(*)
    public static String Address(String str){
        String address = new String();
        String is_address ="(?:(北京|天津|上海|重庆|台湾|.+(省|自治区|特别行政区))(?:市)?.+(市|自治州).+(区|县|旗)?.+(?:(镇|乡|街道))?.+(?:(.+[村|社区|街道])).*)" +
                "|(?:(\\u5317\\u4eac|\\u5929\\u6d25|\\u4e0a\\u6d77|\\u91cd\\u5e86|\\u53f0\\u6e7e|.+(\\u7701|\\u81ea\\u6cbb\\u533a|\\u7279\\u522b\\u884c\\u653f\\u533a))(?:\\u5e02)?.+(\\u5e02|\\u81ea\\u6cbb\\u5dde).+(\\u533a|\\u53bf|\\u65d7)?.+(?:(\\u9547|\\u4e61|\\u8857\\u9053))?.+(?:(.+[\\u6751|\\u793e\\u533a|\\u8857\\u9053])).*)";
        Matcher matcher = Pattern.compile(is_address).matcher(str);
        while (matcher.find()){
            address+=matcher.group();
        }
        return address;
    }
    //身份证匹配
    public static String IdCard(String str){
        ArrayList<String> id = new ArrayList<>();
        String is_id = "\\b[1-9]\\d{5}(?:19|20)\\d\\d(?:0[1-9]|1[012])(?:0[1-9]|[12]\\d|3[01])\\d{3}(?:\\d|X|x)";
        Matcher matcher = Pattern.compile(is_id).matcher(str);
        while (matcher.find()){
            id.add(matcher.group());
        }
        // 去重
        List<String> id_rd = Re.removeDuplicate(id);
        return  StringUtils.strip(id_rd.toString(),"[]");
    }
    //特殊字段匹配
    public static String Password(String str){
        ArrayList<String> pwd = new ArrayList<>();
        String is_pwd = "(?:\"pwd\"|\"password\":|pwd=|password=|config/api" +
                "|method: 'get'|method: 'post'|method: \"get\"|method: \"post\"" +
                "|service\\.httppost|service\\.httpget|\\$\\.ajax|http\\.get\\(\"|http\\.post\\(\"" +
                "rememberMe=delete|[A|a]ccess[K|k]ey|[A|a]ccess[T|t]oken|api_secret|app_secret|(ey[A-Za-z0-9_\\/+-]{34,}\\.[A-Za-z0-9._\\/+-]*))";
        Matcher matcher = Pattern.compile(is_pwd).matcher(str);
        while (matcher.find()){
            pwd.add(matcher.group());
        }
        // 去重
        List<String> pwd_rd = Re.removeDuplicate(pwd);
        return  StringUtils.strip(pwd_rd.toString(),"[]");

    }
    // ip地址匹配
    public static String IP(String str){
        ArrayList<String> ip = new ArrayList<>();
//        String is_ip = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
        String is_ip = "\\b(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0]|[1-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
        Matcher matcher = Pattern.compile(is_ip).matcher(str);
        while (matcher.find()){
            ip.add(matcher.group());
        }
        // 去重
        List<String> ip_rd = Re.removeDuplicate(ip);
        return  StringUtils.strip(ip_rd.toString(),"[]");
    }
    // 内网ip匹配
    public static boolean in_ip(String str){
        String in_ip = "\\b(?:(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}))|(?:172\\.(?:(?:1[6-9])|(?:2\\d)|(?:3[01]))\\.\\d{1,3}\\.\\d{1,3})|(?:192\\.168\\.\\d{1,3}\\.\\d{1,3})";
        Matcher matcher = Pattern.compile(in_ip).matcher(str);
        if (matcher.find()){
            return true;
        }
        return false;
    }
    //邮箱匹配
    public static String Email(String str){
        ArrayList<String> email = new ArrayList<>();
        String is_email = "\\b[\\w-]+(?:\\.[\\w-]+)*@([\\w](?:[\\w-]*[\\w])?\\.)+(?:((?!png))((?!jpg))((?!jpeg))((?!gif))((?!ico))((?!html))((?!js))((?!css)))[A-Za-z]{2,6}";
        Matcher matcher = Pattern.compile(is_email).matcher(str);
        while (matcher.find()){
            email.add(matcher.group());
        }
        // 去重
        List<String> email_rd = Re.removeDuplicate(email);
        return  StringUtils.strip(email_rd.toString(),"[]");
    }

    // js路径匹配
    public static String Path(String str){
        ArrayList<String> path = new ArrayList<>();
        String path1 = new String();
		// String is_path = "[\"|'](?:(\\.)*/|/)[0-9a-zA-Z.]+(?:((/[\\w,\\?,-,_,.]*)+)|[\"|'])";
        String is_path = "[\"|'](/[0-9a-zA-Z.]+(?:/[\\w,\\?,-,\\.,_]*?)+)[\"|']";
        Matcher matcher = Pattern.compile(is_path).matcher(str);
        while (matcher.find()){
                path.add(matcher.group());
        }
        // 去重代码
        List<String> path_rd = Re.removeDuplicate(path);
        for (String s : path_rd){
            path1 += StringUtils.strip(s,"\"'")+'\n';
        }
//        return  StringUtils.strip(path1,"[]");
        return path1;
    }
    public static String Url(String str){
        ArrayList<String> url = new ArrayList<>();
        String url1 = new String();
        String is_url = "([\"|'](http|https):\\/\\/([\\w.]+\\/?)\\S*?[\"|'])";
        Matcher matcher = Pattern.compile(is_url).matcher(str);
        while (matcher.find()){
            url.add(matcher.group());
        }
        // 去重代码
        List<String> path_rd = Re.removeDuplicate(url);
        for (String s : path_rd){
            url1 += StringUtils.strip(s,"\"'")+'\n';
        }
        return url1;
    }
        // 判断javascript文件
    public static boolean js (String headers,byte[] content){
        if (headers.contains("/javascript")||headers.contains("/x-javascript") ){
            if (Re.Path(new String(content)).length() != 0){
                return true;
            }
        }
        return false;
    }
    // 去重代码
    public static List<String> removeDuplicate (ArrayList<String> strings){
    List<String> list2 = new ArrayList<String>(strings);
    list2 = list2.stream().distinct().collect(Collectors.toList());
    return list2;
    }

}
