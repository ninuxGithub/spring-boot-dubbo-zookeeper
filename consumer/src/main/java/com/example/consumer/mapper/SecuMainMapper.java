package com.example.consumer.mapper;

import com.example.consumer.bean.SecuMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenzm
 * @date 2019-4-2
 * @description 作用
 */

@Mapper
@Repository
public interface SecuMainMapper {


    @Select("SELECT ID,InnerCode,CompanyCode,SecuCode,ChiName,ChiNameAbbr,SecuAbbr," +
            "ChiSpelling,SecuMarket,SecuCategory,ListedDate,ListedState " +
            "from SecuMain where SecuMarket in (83,90) and SecuCategory in (1,2,6)")
    public List<SecuMain> loadSecuMainTable();

    @Select("SELECT ID,InnerCode,CompanyCode,SecuCode,ChiName,ChiNameAbbr,SecuAbbr," +
            "ChiSpelling,SecuMarket,SecuCategory,ListedDate,ListedState " +
            "from SecuMain where SecuMarket in (#{sm}) and SecuCategory in (#{sc})")
    public List<SecuMain> loadSecuMainTableParam(@Param("sm") String sm, @Param("sc") String sc);


    /**
     * @param params 可能包含的参数secuMarket-->List<Integer> , secuCategory-->List<Integer>
     * @return
     */
    @SelectProvider(type = SecuMainQueryProvider.class, method = "queryByParams")
    public List<SecuMain> loadSecuMainTableByParams(Map<String, Object> params);

    class SecuMainQueryProvider {
        public String queryByParams(Map<String, Object> params) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ID,InnerCode,CompanyCode,SecuCode,ChiName,ChiNameAbbr,SecuAbbr,")
                    .append("ChiSpelling,SecuMarket,SecuCategory,ListedDate,ListedState ")
                    .append("from SecuMain where 1=1 ");

            if (params.containsKey("secuMarket")) {
                List<Integer> list = (List<Integer>) params.get("secuMarket");
                String str = list.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(","));
                sql.append(" and SecuMarket in (" + str + ") ");
            }
            if (params.containsKey("secuCategory")) {
                List<Integer> list = (List<Integer>) params.get("secuCategory");
                String str = list.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(","));
                sql.append(" and SecuCategory in (" + str + ") ");
            }
            return sql.toString();
        }
    }

}




