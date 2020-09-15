package ice.util;

/**
 * User: Mike.Hu
 * Date: 11-12-29
 * Time: 下午5:22
 */
public interface Entity {

    /**
     * 准备解析
     */
    void beforeParse();

    /**
     * 通知某个标签开始
     *
     * @param statue
     */
    void onStartTag(String statue);

    /**
     * 根据XML标签设置实体类的相应属性.
     *
     * @param tag   XML标签
     * @param value XML数据值
     * @return 属性是否设置成功。当实体类不存在此属性时返回FALSE
     */
    void setAttributeByTag(String tag, String value);

    /**
     * 在XML中带属性的标签用此方法设置相应实体属性
     *
     * @param name  属性名 格式：XML属性所属标签名称+“_”+XML属性名称
     * @param value
     * @return
     */
    void setAttributeByAtt(String name, String value);

    /**
     * 结束标签
     *
     * @param tag
     */
    void noticeEnd(String tag);

}
