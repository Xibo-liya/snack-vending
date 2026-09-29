package com.snackvending.service;

import com.snackvending.entity.*;
import com.snackvending.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitService implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final BannerRepository bannerRepo;
    private final DeviceRepository deviceRepo;

    public DataInitService(CategoryRepository categoryRepo, ProductRepository productRepo,
                           BannerRepository bannerRepo, DeviceRepository deviceRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.bannerRepo = bannerRepo;
        this.deviceRepo = deviceRepo;
    }

    @Override
    public void run(String... args) {
        if (categoryRepo.count() == 0) initCategories();
        if (productRepo.count() == 0) initProducts();
        if (bannerRepo.count() == 0) initBanners();
        if (deviceRepo.count() == 0) initDevice();
    }

    private void initCategories() {
        String[][] data = {
                {"all", "全部", "Menu", "1"},
                {"puff", "膨化食品", "Cpu", "2"},
                {"drink", "饮料", "Coffee", "3"},
                {"candy", "糖果", "Sugar", "4"},
                {"braised", "卤味", "Dish", "5"},
                {"snack", "零食小吃", "Food", "6"}
        };
        for (String[] d : data) {
            Category c = new Category();
            c.setId(d[0]);
            c.setName(d[1]);
            c.setIcon(d[2]);
            c.setSortOrder(Integer.parseInt(d[3]));
            categoryRepo.save(c);
        }
    }

    private void initProducts() {
        Object[][] data = {
                // name, price, stock, image, desc, categoryId
                {"乐事原味薯片", 8.5, 12, "🥔", "经典原味，香脆可口", "puff"},
                {"乐事黄瓜味薯片", 8.5, 8, "🥒", "清爽黄瓜风味", "puff"},
                {"上好佳虾片", 6.0, 15, "🦐", "鲜虾味脆片", "puff"},
                {"可比克薯片", 7.0, 0, "🍟", "番茄味薯片", "puff"},
                {"浪味仙", 5.5, 20, "🌽", "蔬菜味膨化", "puff"},
                {"可口可乐 330ml", 4.0, 30, "🥤", "冰镇畅爽", "drink"},
                {"雪碧 330ml", 4.0, 25, "🍋", "柠檬汽水", "drink"},
                {"农夫山泉 550ml", 2.5, 40, "💧", "天然矿泉水", "drink"},
                {"康师傅冰红茶", 4.5, 18, "🍵", "柠檬红茶", "drink"},
                {"红牛能量饮料", 6.0, 10, "⚡", "提神醒脑", "drink"},
                {"阿尔卑斯牛奶糖", 12.0, 14, "🍬", "香浓牛奶味", "candy"},
                {"徐福记酥糖", 15.0, 9, "🧁", "花生酥心糖", "candy"},
                {"德芙巧克力", 18.0, 7, "🍫", "丝滑牛奶巧克力", "candy"},
                {"不二家棒棒糖", 9.9, 22, "🍭", "水果味棒棒糖", "candy"},
                {"周黑鸭鸭脖", 19.9, 6, "🦆", "麻辣鲜香", "braised"},
                {"绝味鸭翅", 16.0, 4, "🍗", "香辣卤味", "braised"},
                {"盐焗鸡爪", 12.0, 0, "🐔", "皮脆肉嫩", "braised"},
                {"卤蛋", 3.0, 30, "🥚", "五香卤蛋", "braised"},
                {"卫龙魔芋爽", 5.0, 25, "🌶️", "酸辣开胃", "snack"},
                {"三只松鼠每日坚果", 9.9, 12, "🥜", "混合坚果", "snack"},
                {"辣条大面筋", 3.5, 35, "🍜", "经典辣条", "snack"},
                {"泡椒凤爪", 8.0, 16, "🐾", "酸辣爽脆", "snack"}
        };
        for (Object[] d : data) {
            Product p = new Product();
            p.setName((String) d[0]);
            p.setPrice((Double) d[1]);
            p.setStock((Integer) d[2]);
            p.setImage((String) d[3]);
            p.setDescription((String) d[4]);
            p.setCategoryId((String) d[5]);
            productRepo.save(p);
        }
    }

    private void initBanners() {
        Object[][] data = {
                {"新品尝鲜", "三只松鼠每日坚果 限时特惠 ¥9.9", "linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)", "🎁", 1},
                {"满减优惠", "全场满30减5，满50减10", "linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)", "💰", 2},
                {"夏日畅饮", "冰镇饮料全天供应，畅爽一夏", "linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)", "🧊", 3}
        };
        for (Object[] d : data) {
            Banner b = new Banner();
            b.setTitle((String) d[0]);
            b.setDescription((String) d[1]);
            b.setBgColor((String) d[2]);
            b.setIcon((String) d[3]);
            b.setSortOrder((Integer) d[4]);
            bannerRepo.save(b);
        }
    }

    private void initDevice() {
        Device d = new Device();
        d.setDeviceNo("SV-2026-001");
        d.setOnline(true);
        d.setStatus("normal");
        d.setTemperature("4°C");
        d.setMessage("设备运行正常");
        deviceRepo.save(d);
    }
}
