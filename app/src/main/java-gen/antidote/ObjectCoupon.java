package antidote;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table OBJECT_COUPON.
 */
public class ObjectCoupon {

    private Long id;
    private String code;
    private Integer discount_type;
    private Float discount_percent;
    private Float discount_amount;
    private Float minimum_amount;
    private Integer active;

    public ObjectCoupon() {
    }

    public ObjectCoupon(Long id) {
        this.id = id;
    }

    public ObjectCoupon(Long id, String code, Integer discount_type, Float discount_percent, Float discount_amount, Float minimum_amount, Integer active) {
        this.id = id;
        this.code = code;
        this.discount_type = discount_type;
        this.discount_percent = discount_percent;
        this.discount_amount = discount_amount;
        this.minimum_amount = minimum_amount;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(Integer discount_type) {
        this.discount_type = discount_type;
    }

    public Float getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(Float discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Float getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(Float discount_amount) {
        this.discount_amount = discount_amount;
    }

    public Float getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(Float minimum_amount) {
        this.minimum_amount = minimum_amount;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

}