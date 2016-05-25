package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(1, "antidote");

        // gen User
        Entity user = schema.addEntity("ObjectUser");
        //user.addIdProperty();
        user.addLongProperty("id").primaryKey();
        user.addStringProperty("email");
        user.addStringProperty("idFacebook");
        user.addStringProperty("firstName");
        user.addStringProperty("lastName");
        user.addStringProperty("phone");
        user.addStringProperty("address");
        user.addStringProperty("city");
        user.addStringProperty("country");
        user.addStringProperty("postCode");
        user.addStringProperty("isActive");
        user.addStringProperty("image");

        // gen Product
        Entity product = schema.addEntity("ObjectProduct");
        product.addLongProperty("id").primaryKey();
        product.addLongProperty("categoryID");
        product.addStringProperty("name");
        product.addStringProperty("slug");
        product.addStringProperty("description");
        product.addFloatProperty("regularPrice");
        product.addFloatProperty("salePrice");
        product.addStringProperty("priceRange");
        product.addStringProperty("isActive");
        product.addStringProperty("image");

        // gen category
        Entity category = schema.addEntity("ObjectCategory");
        category.addLongProperty("id").primaryKey();
        category.addStringProperty("name");
        category.addStringProperty("slug");
        category.addStringProperty("parentID");
        category.addStringProperty("shortDescription");
        category.addStringProperty("fullDescription");
        category.addStringProperty("thumbnail");
        category.addStringProperty("image");
        category.addStringProperty("isActive");

        // gen country
        Entity country = schema.addEntity("ObjectCountry");
        country.addLongProperty("id").primaryKey().autoincrement();
        country.addStringProperty("code");
        country.addStringProperty("slug");
        country.addStringProperty("name");

        // gen comment
        Entity comment = schema.addEntity("ObjectComment");
        comment.addLongProperty("id").primaryKey();
        comment.addIntProperty("productID");
        comment.addIntProperty("userID");
        comment.addStringProperty("comment");
        comment.addStringProperty("createDate");
        comment.addStringProperty("firstName");
        comment.addStringProperty("lastName");
        comment.addStringProperty("image");
        comment.addStringProperty("idFacebook");

        // gen option
        Entity option = schema.addEntity("ObjectOption");
        option.addLongProperty("id").primaryKey();
        option.addStringProperty("name");
        option.addStringProperty("slug");

        // gen product option
        Entity product_option = schema.addEntity("ObjectProductOption");
        product_option.addLongProperty("id").primaryKey();
        product_option.addLongProperty("productID");
        product_option.addLongProperty("optionID");
        product_option.addIntProperty("option_index");
        product_option.addStringProperty("value");
        product_option.addFloatProperty("regularPrice");
        product_option.addFloatProperty("salePrice");

        // gen group product
        Entity group_product = schema.addEntity("ObjectGroupProduct");
        group_product.addLongProperty("id").primaryKey().autoincrement();
        group_product.addIntProperty("groupID");
        group_product.addStringProperty("name");
        group_product.addLongProperty("productID");
        group_product.addIntProperty("timer");
        group_product.addStringProperty("image");
        group_product.addIntProperty("repeat_day");
        group_product.addStringProperty("ingredient");
        group_product.addStringProperty("description");

        // gen group timer
        Entity group_timer = schema.addEntity("ObjectGroupTimer");
        group_timer.addLongProperty("id").primaryKey().autoincrement();
        group_timer.addIntProperty("groupID");
        group_timer.addLongProperty("productID");
        group_timer.addLongProperty("time");

        // gen cart
        Entity cart = schema.addEntity("ObjectCart");
        cart.addLongProperty("id").primaryKey().autoincrement();
        cart.addLongProperty("productID");
        cart.addStringProperty("productName");
        cart.addIntProperty("quantity");
        cart.addFloatProperty("regularPrice");
        cart.addFloatProperty("salePrice");
        cart.addLongProperty("idProductOption");
        cart.addStringProperty("nameProductOption");

        // gen Order Delivery
        Entity order_delivery = schema.addEntity("ObjectOrderDelivery");
        order_delivery.addLongProperty("id").primaryKey().autoincrement();
        order_delivery.addStringProperty("orderDate");//
        order_delivery.addStringProperty("orderStatus");//
        order_delivery.addStringProperty("customerID");//
        order_delivery.addStringProperty("shippingFirstName");//
        order_delivery.addStringProperty("shippingLastName");//
        order_delivery.addStringProperty("shippingCompany");//
        order_delivery.addStringProperty("shippingAddress");//
        order_delivery.addStringProperty("shippingApartment");//
        order_delivery.addStringProperty("shippingCity");//
        order_delivery.addStringProperty("shippingCountry");//
        order_delivery.addStringProperty("shippingPostCode");//
        order_delivery.addStringProperty("shippingEmail");//
        order_delivery.addStringProperty("shippingPhone");//
        order_delivery.addStringProperty("shippingNote");//
        order_delivery.addStringProperty("subTotal");//
        order_delivery.addStringProperty("discount");//
        order_delivery.addStringProperty("shippingType");//
        order_delivery.addStringProperty("total");//
        order_delivery.addStringProperty("order_product");//
        order_delivery.addStringProperty("couponid");//

        // gen banner
        Entity banner = schema.addEntity("ObjectBanner");
        banner.addLongProperty("id").primaryKey();
        banner.addStringProperty("image");
        banner.addStringProperty("title");

        // gen order history
        Entity order_history = schema.addEntity("ObjectOrderHistory");
        order_history.addLongProperty("id").primaryKey();
        order_history.addFloatProperty("total");
        order_history.addStringProperty("createDate");

        // gen coupon
        Entity coupon = schema.addEntity("ObjectCoupon");
        coupon.addLongProperty("id").primaryKey();
        coupon.addStringProperty("code");
        coupon.addIntProperty("discount_type");
        coupon.addFloatProperty("discount_percent");
        coupon.addFloatProperty("discount_amount");
        coupon.addFloatProperty("minimum_amount");
        coupon.addIntProperty("active");

        // gen faq
        Entity faq = schema.addEntity("ObjectFaq");
        faq.addLongProperty("id").primaryKey();
        faq.addLongProperty("userID");
        faq.addStringProperty("question");
        faq.addStringProperty("answer");

        // gen faq_comment
        Entity faq_comment = schema.addEntity("ObjectFaqComment");
        faq_comment.addLongProperty("id").primaryKey();
        faq_comment.addLongProperty("faqsID");
        faq_comment.addLongProperty("userID");
        faq_comment.addStringProperty("comment");
        faq_comment.addStringProperty("firstName");
        faq_comment.addStringProperty("lastName");
        faq_comment.addStringProperty("image");
        faq_comment.addStringProperty("idFacebook");
        faq_comment.addStringProperty("createDate");

        // gen faqv2
        Entity faqv2 = schema.addEntity("ObjectFaqV2");
        faqv2.addLongProperty("id").primaryKey();
        faqv2.addStringProperty("question");
        faqv2.addStringProperty("answer");
        faqv2.addIntProperty("sort");

        new DaoGenerator().generateAll(schema, args[0]);
    }

    // sample
    public static void gen1box_manyitems() {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        Property itemId = item.addIdProperty().getProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property boxId = item.addLongProperty("boxId").getProperty();
        ToMany boxToItem = box.addToMany(item, boxId);
        boxToItem.orderDesc(itemId);

    }

    // sample
    public static void gen1box1item() {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        item.addIdProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property itemId = box.addLongProperty("itemId").getProperty();
        box.addToOne(item, itemId);

    }
}