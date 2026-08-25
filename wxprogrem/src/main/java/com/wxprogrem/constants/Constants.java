package com.wxprogrem.constants;

public class Constants {

    //项目加密密钥
    public static final  String KEY="mykeys";

    //redis缓存 dishId专有前缀
    public static final  String  DISHID="dish_id";

    //redis缓存 dishTypeId专有前缀
    public static final  String  DISHTYPEID="dish_type_id";

    //redis缓存 dishOnShow专有前缀
    public static final  String  DISHONSHOW="dish_onshow";

    //redis缓存 dishTypeOnShow专有前缀
    public static final  String  DISHTYPEONSHOW="dish_type_onshow";

    //redis缓存优惠券秒杀库存inventory专用前缀
    public static final  String  STOCKSECKVOUCHERID="stock_seckill_voucher_id";

    //redis缓存优惠券秒杀已抢票用户集合专用前缀
    public static final  String  BOUGHTSECKILLVOUCHER="bought_seckill_voucher_id";

    //优惠券秒杀入库扣减锁专用前缀
    public static final  String  BOUGHTSECKILLLOCK="bought_seckill_lock_id";

    //redis缓存不同商品分类和启售状态信息专用前缀
    public static final  String  DISHTYPEIDWITHSTATUS="dishtypeid_status";

    //redis缓存所有套餐信息专用前缀
    public static  final String SETMEALALL="setmeal_all";

    //redis根据id缓存套餐信息专用前缀
    public static  final String SETMEALID="setmeal_id";

    //redis根据name缓存套餐信息专用前缀
    public static  final String SETMEALNAME="setmeal_name";

    //redis根据套餐id缓存套餐包含的商品信息专用前缀
    public static  final String SETMEALINCLUDEDISHBYSETMEALID="setmeal_include_dish_by_setmeal_id";

    //管理员套餐CRUD专用锁
    public static final String  SETMEALLOCK = "setmeal_lock_setmeal_id";

    //用户端菜品分类模块dishTypeOnShow缓存异步刷新锁
    public static  final String DISHTYPEONSHOWLOCK = "dish_type_onshow_lock";

    //用户端订单接口读写锁
    public static  final String ORDEROPERATIONREADANDWRITELOCK = "ORDEROPERATIONREADANDWRITELOCK";

    //redis根据菜品ID缓存菜品库存
    public static  final String DISHINVENTORYID = "dish_inventory_id";

}
