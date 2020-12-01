package javabase.lorenwang.network;

import org.junit.Test;

/**
 * 功能作用：
 * 创建时间：2020-12-01 1:48 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class JnlwHttpClientReqTest {

    @Test
    public void setHttpclient() {
        JnlwHttpClientReqFactory.getHttpClientRequest().sendRequest(new JnlwNetworkReqConfig.Build()
                .setBaseUrl("http://www.ip38.com/")
                .setNetworkTypeEnum(JnlwNetworkTypeEnum.GET)
                .setNetworkCallback(new JnlwNetworkCallback() {
                    @Override
                    public void success(String protocol, int major, int minor, String data) {
                        System.out.println("请求成功:" + data);
                    }

                    @Override
                    public void fail(String protocol, int major, int minor, int failStatuesCode) {
                        System.out.println("请求失败:" + failStatuesCode);
                    }

                    @Override
                    public void error(Exception e) {
                        System.out.println("请求异常:" + e.getMessage());
                    }
                }).build());
    }

    @Test
    public void setHttpclientPost() {
        JnlwHttpClientReqFactory.getHttpClientRequest().sendRequest(new JnlwNetworkReqConfig.Build()
                .setBaseUrl("http://localhost:5432")
                .setRequestUrl("/customServiceTest/setHomeData")
                .addRequestDataParam("type", "1")
                .setRequestDataJson("{\n" +
                        "  \"code\": \"200\",\n" +
                        "  \"data\": {\n" +
                        "    \"moduleList\": [\n" +
                        "      {\n" +
                        "        \"baseModuleInfo\": {\n" +
                        "          \"type\": 6,\n" +
                        "          \"isDisplayMore\": 1,\n" +
                        "          \"linkInfo\": \"aaa\",\n" +
                        "          \"linkType\": 4,\n" +
                        "          \"subTitle\": \"图文主题显示图文主题显示\",\n" +
                        "          \"titleColor\": \"#983223\"\n" +
                        "        },\n" +
                        "        \"moduleDetail\": {\n" +
                        "          \"themeList\": [\n" +
                        "            {\n" +
                        "              \"themeName\": \"不知道叫啥\",\n" +
                        "              \"subTitle\": \"副标题\",\n" +
                        "              \"indexPicUrl\": \"https://timgsa.baidu" +
                        ".com/timg?image&quality=80&size=b9999_10000&sec=1604897368452&di=48af9910fd3e1e0d84fbc69cc38ea748&imgtype=0&src=http%3A%2F" +
                        "%2Fattach.bbs.miui.com%2Fforum%2F201401%2F23%2F095609lsejfi4thjrrwydj.jpg\",\n" +
                        "              \"pageCode\": \"aaa\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"themeName\": \"不知道叫啥\",\n" +
                        "              \"subTitle\": \"副标题\",\n" +
                        "              \"indexPicUrl\": \"https://timgsa.baidu" +
                        ".com/timg?image&quality=80&size=b9999_10000&sec=1604897368452&di=48af9910fd3e1e0d84fbc69cc38ea748&imgtype=0&src=http%3A%2F" +
                        "%2Fattach.bbs.miui.com%2Fforum%2F201401%2F23%2F095609lsejfi4thjrrwydj.jpg\",\n" +
                        "              \"pageCode\": \"aaa\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"themeName\": \"不知道叫啥\",\n" +
                        "              \"subTitle\": \"副标题\",\n" +
                        "              \"indexPicUrl\": \"https://timgsa.baidu" +
                        ".com/timg?image&quality=80&size=b9999_10000&sec=1604897368452&di=48af9910fd3e1e0d84fbc69cc38ea748&imgtype=0&src=http%3A%2F" +
                        "%2Fattach.bbs.miui.com%2Fforum%2F201401%2F23%2F095609lsejfi4thjrrwydj.jpg\",\n" +
                        "              \"pageCode\": \"aaa\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"themeName\": \"不知道叫啥\",\n" +
                        "              \"subTitle\": \"副标题\",\n" +
                        "              \"indexPicUrl\": \"https://timgsa.baidu" +
                        ".com/timg?image&quality=80&size=b9999_10000&sec=1604897368452&di=48af9910fd3e1e0d84fbc69cc38ea748&imgtype=0&src=http%3A%2F" +
                        "%2Fattach.bbs.miui.com%2Fforum%2F201401%2F23%2F095609lsejfi4thjrrwydj.jpg\",\n" +
                        "              \"pageCode\": \"aaa\"\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}\n" +
                        "\n")
                .setNetworkTypeEnum(JnlwNetworkTypeEnum.POST)
                .setNetworkCallback(new JnlwNetworkCallback() {
                    @Override
                    public void success(String protocol, int major, int minor, String data) {
                        System.out.println("请求成功:" + data);
                    }

                    @Override
                    public void fail(String protocol, int major, int minor, int failStatuesCode) {
                        System.out.println("请求失败:" + failStatuesCode);
                    }


                    @Override
                    public void error(Exception e) {
                        System.out.println("请求异常:" + e.getMessage());
                    }
                }).build());
    }
}
