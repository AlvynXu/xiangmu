import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.guangxuan.Application;
import com.guangxuan.constant.RedisConstant;
import com.guangxuan.distribution.Distribution;
import com.guangxuan.dto.PromoterDTO;
import com.guangxuan.enumration.AreaLevel;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.Data;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.net.ssl.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class CodeGenerator {

    @Resource
    private HikariDataSource hikariDataSource;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void fake() throws InterruptedException {

        Random random = new Random();
        while (true) {
            int booth = random.nextInt(3);
            int sleepTime = random.nextInt(5) + 5;
            int count = (int) redisTemplate.opsForValue().get(RedisConstant.FAKE_SOLD_BOOTH_COUNT);
            redisTemplate.opsForValue().set(RedisConstant.FAKE_SOLD_BOOTH_COUNT, booth + count);
            System.out.println("增加" + booth + "时间" + sleepTime);
            Thread.sleep(sleepTime * 1000);
        }
    }

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    @Test
    public void main11() {

        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
        tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
        tableFillList.add(new TableFill("is_deleted", FieldFill.INSERT));
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D:\\src");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setSwagger2(true); // swagger
        //gc.setKotlin(true);//是否生成 kotlin 代码
        gc.setAuthor("zhuolin");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        /*dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });*/
        dsc.setDriverName(hikariDataSource.getDriverClassName());
        dsc.setUsername(hikariDataSource.getUsername());
        dsc.setPassword(hikariDataSource.getPassword());
        dsc.setUrl(hikariDataSource.getJdbcUrl());
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setEntityLombokModel(true); // lombok
        //strategy.setInclude(new String[]{"app_certificate"}); // 需要生成的表,注释掉生成全部表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        //strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        //strategy.setEntityBuilderModel(true);
        strategy.setTableFillList(tableFillList).setTablePrefix("guangxuan_");
        strategy.setInclude("item_leave_message");
        mpg.setStrategy(strategy);

        // 包配置

        mpg.setPackageInfo(new PackageConfig()
                .setParent("com.guangxuan")
                .setController("controller")
                .setEntity("model")
                .setService("service")
                .setServiceImpl("service.impl")
                .setMapper("mapper")
                .setXml("mapper/xml")
        );

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】  $E{cfg.abc}
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 自定义 xxListIndex.html 生成
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        focList.add(new FileOutConfig("/templates/list.html.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "E://src//html//" + tableInfo.getEntityName() + "ListIndex.html";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 关闭默认 xml 生成，调整生成 至 根目录
        /*TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);*/

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/controller.java.vm");
        tc.setService("/templates/service.java.vm");
        tc.setServiceImpl("/templates/serviceImpl.java.vm");
        tc.setEntity("/templates/entity.java.vm");
        tc.setMapper("/templates/mapper.java.vm");
        tc.setXml("/templates/mapper.xml.vm");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

    @Resource
    private Distribution distribution;

    @Test
    @Transactional
    public void setDistribution() throws NoSuchFieldException, IllegalAccessException {
        // 购买会员
//        distribution.distribution(8L, new BigDecimal(100000L), null, null, null);
        // 购买地主
        distribution.distribution(7L, new BigDecimal(12980L), 1, "430121", null);
        // 购买展位
        distribution.distribution(7L, new BigDecimal(680), -1, "1017545", 1L);

    }

    @Resource
    private StreetService streetService;

    @Resource
    private BoothService boothService;

    @Test
    public void main() throws IOException, InterruptedException {
        List<Street> streets = streetService.list();
        Map<Long, Integer> map = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (Street street : streets) {
            executorService.execute(() -> {
                IPage<Booth> boothPage = boothService.page(new Page<>(1, 10),
                        new LambdaQueryWrapper<Booth>().eq(Booth::getStreetId, street.getId()));
                List<Booth> booths = boothPage.getRecords();
                booths.forEach(a -> a.setSaved(true));
                boothService.updateBatchById(booths);
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        ;
        Thread.sleep(500000L);
    }

    @Resource
    private AreaService areaService;


    //    static volatile
    int i = 0;

    @Test
    public void generateBooth() throws InterruptedException {
        List<Area> areas = areaService.list();
        Map<String, Area> areaMap = areas.stream().collect(Collectors.toMap(Area::getCode, area -> area));
        List<Street> streets = streetService.list();
        Map<Long, String> map = new HashMap<>();
//        Map<String, List<Street>> streetMap = new HashMap<>();
        for (Street street : streets) {
            Area district = areaMap.get(street.getAreaCode());
            Area city = areaMap.get(district.getParentCode());
            if (city.getName().equals("市辖区") || city.getName().equals("市辖县")) {
                Area province = areaMap.get(city.getParentCode());
                map.put(street.getId(), getPinYinHeadChar(province.getName()));
            } else {
                map.put(street.getId(), getPinYinHeadChar(city.getName()));
            }
        }
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        ConcurrentMap<String, Integer> countMap = new ConcurrentHashMap<>();
        for (i = 0; i < 100; i++) {
            executorService.execute(() -> {
                for (Street street : streets) {

                    String pinyinHeader = map.get(street.getId());
                    String header = pinyinHeader.substring(0, 2);
                    Integer num = 0;
                    synchronized (countMap) {
                        if (!countMap.containsKey(header)) {
                            countMap.put(header, 0);
                        }
                        num = countMap.get(header);
                        countMap.replace(header, countMap.get(header) + 1);
                    }
                    String code = header + autoGenericCode(num + 1, 8);
                    Booth booth = new Booth();
                    booth.setStatus(0);
                    booth.setSaved(i < 10);
                    booth.setStreetId(street.getId());
                    booth.setBoothCode(code);
                    boothService.save(booth);
                    try {
                        Thread.sleep(3L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        Thread.sleep(10000000L);
    }

    private String autoGenericCode(Integer code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", code);

        return result;
    }


    @Test
    public void main1() throws InterruptedException {
        List<Street> streets = streetService.list();
        for (Street street : streets) {
            List<Booth> booths = boothService.page(new Page<Booth>(1, 10),
                    new LambdaQueryWrapper<Booth>().eq(Booth::getStreetId, street.getId())).getRecords();
            booths.forEach(a -> a.setSaved(true));
            boothService.updateBatchById(booths);
        }
    }


    /**
     * 提取每个汉字的首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            //提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }


    @Test
    public void main2() throws InterruptedException {

    }

    List<String> strings = new ArrayList<>();

    private String generateCode(String header) {
        String s = "0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer(header.substring(0, 2));
        stringBuffer.append("00");
        for (int i = 0; i < 5; i++) {
            stringBuffer.append(s.charAt(random.nextInt(s.length())));
        }
        String code = stringBuffer.toString();
        if (strings.contains(code)) {
            generateCode(header);
        }
        strings.add(code);
        return code;

    }

    @Resource
    private HeadlinesService headlinesService;


    @Test
    public void main3() throws IOException {
        List<Headlines> headliness = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Headlines headlines = new Headlines();
            headlines.setCreateTime(new Date());
            headlines.setPrice(new BigDecimal(1));
            headlines.setSort(i + 1);
            headlines.setType(1);
            headliness.add(headlines);
        }
        headlinesService.saveBatch(headliness);
    }


    @Test
    public void main30() throws IOException {
        String path = "D:\\zhuolin\\软件";
        File file = new File(path);
        for (File file1 : file.listFiles()) {
            StringBuilder stringBuilder = new StringBuilder();
            test(file1, stringBuilder);
            String str = stringBuilder.toString();

            String saveFilePath = path + File.separator + file1.getName() + ".docx";
            XWPFDocument doc = new XWPFDocument();
            XWPFParagraph p1 = doc.createParagraph();
            XWPFRun r4 = p1.createRun();
            r4.setFontSize(18);
            String s[] = str.split("\r\n");
            for (int i = 0; i < s.length; i++) {
                r4.setText(str);
                r4.addBreak();
            }
            FileOutputStream out = new FileOutputStream(saveFilePath);
            doc.write(out);
            out.close();
        }
    }

    private StringBuilder test(File file, StringBuilder result) throws IOException {
        File[] files = file.listFiles();
        if (files == null) {
            return result;
        }
        for (File file1 : files) {
            if (file1.isDirectory()) {
                if (file1.getName().startsWith(".") || file1.getName().startsWith("images") || file1.getName().startsWith("img")) {
                    continue;
                }
                test(file1, result);
            } else {
                if (file1.getName().startsWith(".")) {
                    continue;
                }
                System.out.println(file1.getName());
                if ((file1.getName().endsWith(".java")
                        || file1.getName().endsWith(".html")
                        || file1.getName().endsWith(".js")
                        || file1.getName().endsWith("wxml"))
                        && !file1.getName().contains("jquery")
                        && !file1.getName().contains("vue")
                        && !file1.getName().contains("png")
                        && !file1.getName().contains("jpg")) {

                    BufferedReader br = new BufferedReader(new FileReader(file1));
                    String s = null;
                    while ((s = br.readLine()) != null) {
                        result.append(s);
                    }
                }
            }
        }
        return result;
    }


    @Test
    public void addPromoter() {
        PromoterDTO PromoterDTO = (com.guangxuan.dto.PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT);
        System.out.println(JSON.toJSONString(PromoterDTO));
    }

//    @Test
//    public void privince() throws IOException {
//        List<Area> areas = getData("https://www.tianzhicun.com");
//        List<Area> list = new ArrayList<>();
//
//        for (Area area : areas) {
//            String code = area.getStreetCode();
//            list.addAll(getData("https://www.tianzhicun.com/province/" + code + "/town"));
//        }
//
//        HSSFWorkbook wb = new HSSFWorkbook();
//        // 全部区域到街道
//        HSSFSheet streetSheet = wb.createSheet("街道");
//        Set<String> streetCodes = new HashSet<>();
//        // 全部区域到区县
//        HSSFSheet districtSheet = wb.createSheet("区县");
//        Set<String> districtCodes = new HashSet<>();
//        // 全部区域到城市
//        HSSFSheet citySheet = wb.createSheet("城市");
//        Set<String> cityCodes = new HashSet<>();
//        // 全部区域到省份
//        HSSFSheet provinceSheet = wb.createSheet("省份");
//        Set<String> provinceCodes = new HashSet<>();
//        for (int i = 0; i < list.size(); i++) {
//
//
//            Area area = list.get(i);
//            if (!streetCodes.contains(area.getStreetCode())) {
//                Street street = new Street();
//                street.setCode(area.getStreetCode());
//                street.setName(area.getStreetName());
//                street.setAreaCode(area.getDistrictCode());
//                street.setStatus(0);
//                streetService.save(street);
////                HSSFRow streetRow = streetSheet.createRow(i);
////                HSSFCell cell00 = streetRow.createCell(0);
////                cell00.setCellValue(area.getProvinceName());
////                HSSFCell cell10 = streetRow.createCell(1);
////                cell10.setCellValue(area.getProvinceCode());
////                HSSFCell cell20 = streetRow.createCell(2);
////                cell20.setCellValue(area.getCityName());
////                HSSFCell cell30 = streetRow.createCell(3);
////                cell30.setCellValue(area.getCityCode());
////                HSSFCell cell40 = streetRow.createCell(4);
////                cell40.setCellValue(area.getDistrictName());
////                HSSFCell cell50 = streetRow.createCell(5);
////                cell50.setCellValue(area.getDistrictCode());
////                HSSFCell cell60 = streetRow.createCell(6);
////                cell60.setCellValue(area.getStreetName());
////                HSSFCell cell70 = streetRow.createCell(7);
////                cell70.setCellValue(area.getStreetCode());
//                streetCodes.add(area.getStreetCode());
//            }
////
////            if (!districtCodes.contains(area.getDistrictCode())) {
////                com.guangxuan.model.Area saveArea = new com.guangxuan.model.Area();
////                saveArea.setLevel(AreaLevel.DISTRICT.getId());
////                saveArea.setCode(area.getDistrictCode());
////                saveArea.setName(area.getDistrictName());
////                saveArea.setParentCode(area.getCityCode());
////                areaService.save(saveArea);
////                HSSFRow streetRow = districtSheet.createRow(districtCodes.size());
////                HSSFCell cell00 = streetRow.createCell(0);
////                cell00.setCellValue(area.getProvinceName());
////                HSSFCell cell10 = streetRow.createCell(1);
////                cell10.setCellValue(area.getProvinceCode());
////                HSSFCell cell20 = streetRow.createCell(2);
////                cell20.setCellValue(area.getCityName());
////                HSSFCell cell30 = streetRow.createCell(3);
////                cell30.setCellValue(area.getCityCode());
////                HSSFCell cell40 = streetRow.createCell(4);
////                cell40.setCellValue(area.getDistrictName());
////                HSSFCell cell50 = streetRow.createCell(5);
////                cell50.setCellValue(area.getDistrictCode());
////                districtCodes.add(area.getDistrictCode());
////            }
////
////            if (!cityCodes.contains(area.getCityCode())) {
////                com.guangxuan.model.Area saveArea = new com.guangxuan.model.Area();
////                saveArea.setLevel(AreaLevel.CITY.getId());
////                saveArea.setCode(area.getCityCode());
////                saveArea.setName(area.getCityName());
////                saveArea.setParentCode(area.getProvinceCode());
////                areaService.save(saveArea);
////                cityCodes.add(area.getCityCode());
////            }
////            if (!provinceCodes.contains(area.getProvinceCode())) {
////
//////                HSSFRow streetRow = provinceSheet.createRow(provinceCodes.size());
//////                HSSFCell cell00 = streetRow.createCell(0);
//////                cell00.setCellValue(area.getProvinceName());
//////                HSSFCell cell10 = streetRow.createCell(1);
//////                cell10.setCellValue(area.getProvinceCode());
////                provinceCodes.add(area.getProvinceCode());
////            }
//        }
//
////        FileOutputStream fout = new FileOutputStream("D:/区域.xls");
////        wb.write(fout);
//
//
//    }

//    private List<Area> getData(String string) {
//        try {
//            URL url;
//            url = new URL(string);
//            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//            X509TrustManager xtm = new X509TrustManager() {
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    // TODO Auto-generated method stub
//                    return null;
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                        throws CertificateException {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                        throws CertificateException {
//                    // TODO Auto-generated method stub
//
//                }
//            };
//
//            TrustManager[] tm = {xtm};
//
//            SSLContext ctx = SSLContext.getInstance("TLS");
//            ctx.init(null, tm, null);
//
//            con.setSSLSocketFactory(ctx.getSocketFactory());
//            con.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String arg0, SSLSession arg1) {
//                    return true;
//                }
//            });
//
//
//            InputStream inStream = con.getInputStream();
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = inStream.read(buffer)) != -1) {
//                outStream.write(buffer, 0, len);
//            }
//            byte[] b = outStream.toByteArray();//网页的二进制数据
//            outStream.close();
//            inStream.close();
//            String rtn = new String(b, "utf-8");
////            System.out.println(rtn);
//            Document document = Jsoup.parse(rtn);
//            //像js一样，通过标签获取title
////            System.out.println(document.getElementsByTag("title").first());
//            //像js一样，通过id 获取文章列表元素对象
//            Element postList = document.getElementById("example");
//            //像js一样，通过class 获取列表下的所有博客
//            Elements postItems = postList.getElementsByTag("tr");
//            //循环处理每篇博客
//            List<Area> list = new ArrayList<>();
//            int i = 0;
//            for (Element postItem : postItems) {
//                i++;
//                if (i < 3) {
//                    continue;
//                }
//                Area area = Area.builder()
//                        .streetName(postItem.getElementsByTag("td").get(0).text())
//                        .streetCode(postItem.getElementsByTag("td").get(1).text())
//                        .districtCode(postItem.getElementsByTag("td").get(5).getElementsByTag("a").attr("href").replace("/province/city/county/", ""))
//                        .districtName(postItem.getElementsByTag("td").get(5).text())
//                        .cityCode(postItem.getElementsByTag("td").get(4).getElementsByTag("a").attr("href").replace("/province/city/", ""))
//                        .cityName(postItem.getElementsByTag("td").get(4).text())
//                        .provinceCode(postItem.getElementsByTag("td").get(3).getElementsByTag("a").attr("href").replace("/province/", ""))
//                        .provinceName(postItem.getElementsByTag("td").get(3).text())
//                        .build();
//                list.add(area);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(string);
//        }
//        return null;
//    }

//    @Data
//    @Builder
//    static class Area {
//        /**
//         * 街道编码
//         */
//        private String streetCode;
//        /**
//         * 街道名称
//         */
//        private String streetName;
//        /**
//         * 区县编码
//         */
//        private String districtCode;
//        /**
//         * 区县名称
//         */
//        private String districtName;
//        /**
//         * 城市编码
//         */
//        private String cityCode;
//        /**
//         * 城市名称
//         */
//        private String cityName;
//        /**
//         * 省份编码
//         */
//        private String provinceCode;
//        /**
//         * 省份名称
//         */
//        private String provinceName;
//
//    }

    @Resource
    private StreetPartnerOrderService streetPartnerOrderService;

    @Resource
    private BoothUsersService boothUsersService;

    @Test
    public void demo() throws IOException {
        Workbook workbook = WorkbookFactory.create(new FileInputStream("D:/区域.xls"));
        Sheet sheet = workbook.getSheet("Sheet2");
        int colum = sheet.getLastRowNum();
        for (int i = 0; i <= colum; i++) {
            Row row = sheet.getRow(i);
            int cellNUmber = row.getLastCellNum();
            Long userId = 0L;
            Long streetId = 0L;
            Integer useStatus = 0;
            Integer isSaved = 0;
            Date createTime = new Date();
            Date endTime = new Date();
            for (int j = 0; j < cellNUmber; j++) {
                Cell userIdCell = row.getCell(4);
                useStatus = new Double(userIdCell.getNumericCellValue()).intValue();
                Cell streetCell = row.getCell(5);
                isSaved = new Double(streetCell.getNumericCellValue()).intValue();
                Cell streetCell1 = row.getCell(6);
                userId = new Double(streetCell1.getNumericCellValue()).longValue();
                createTime = row.getCell(7).getDateCellValue();
                endTime = row.getCell(8).getDateCellValue();
                streetId = new Double(row.getCell(9).getNumericCellValue()).longValue();
            }
            System.out.println(userId + ":" + streetId + ":" + useStatus + ":" + isSaved + ":" + createTime + ":" + endTime);
            Booth booth = boothService.page(new Page<>(1, 1), new LambdaQueryWrapper<Booth>()
                    .eq(Booth::getStreetId, streetId)
                    .eq(Booth::getSaved, isSaved == 1)
                    .eq(Booth::getStatus, 0)).getRecords().get(0);
            booth.setStatus(1);

            booth.setUseStatus(useStatus);
            boothService.updateById(booth);
            BoothUsers boothUsers = new BoothUsers();
            boothUsers.setCreateTime(createTime);
            boothUsers.setExpireTime(endTime);
            boothUsers.setUserId(userId);
            boothUsers.setStatus(1);
            boothUsers.setOrderStatus(9);
            boothUsers.setBoothId(booth.getId());
            Cell userIdCell = row.createCell(11);
            userIdCell.setCellValue(booth.getId());
            boothUsersService.save(boothUsers);
        }
        FileOutputStream fout = new FileOutputStream("D:/区域.xls");
        workbook.write(fout);

    }




}