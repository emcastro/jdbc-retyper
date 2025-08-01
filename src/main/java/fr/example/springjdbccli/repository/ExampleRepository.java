package fr.example.springjdbccli.repository;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.example.springjdbccli.JsonBox;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.DataSource;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ExampleRepository {

  private final JdbcClient jdbcClient;
  private Converter<JsonBox, PGobject> converter;

  //   private final ObjectMapper objectMapper;

  public ExampleRepository(
    DataSource datasource
    // JdbcClient jdbcClient
    // ObjectMapper objectMapper
  ) {
    this.jdbcClient = JdbcClient.create(new MagicDatasource(datasource));
    // this.objectMapper = objectMapper;

    // ConverterRegistry x =
    //   (DefaultConversionService) DefaultConversionService.getSharedInstance();
    this.converter = new Converter<JsonBox, PGobject>() {
      @Override
      public PGobject convert(JsonBox source) {
        var jsonObject = new PGobject();
        jsonObject.setType("jsonb");

        try {
          jsonObject.setValue(source.value());
        } catch (SQLException e) {
          // Cannot happen due to internal PGobject implementation
          throw new RuntimeException(e);
        }
        return jsonObject;
      }
    };
    // x.addConverter(ObjectNode.class, PGobject.class, converter);

    // var converter2 = new Converter<PGobject, ObjectNode>() {
    //     @Override
    //     public ObjectNode convert(PGobject source) {
    //         var value = source.getValue();
    //         try {
    //             return (ObjectNode) com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
    //                 .readTree(value);
    //         } catch (Exception e) {
    //             throw new RuntimeException(e);
    //         }
    //     }
    // };
    // x.addConverter(PGobject.class, ObjectNode.class, converter2);

  }

  //   public void saveExampleData(String id, ObjectNode jsonData) {
  //     String sql = "INSERT INTO example_table (id, data) VALUES (?, ?)";
  //     jdbcTemplate.update(sql, id, jsonData);
  //   }

  public record X(String name) {}

  public record Y(JsonBox name) {}

  public Object work(JsonBox id) {
    String sql = "SELECT (?->'a')::jsonb name";
    return jdbcClient
      .sql(sql)
      .param(
        // new SqlParameterValue(
        //   Types.OTHER,
        // converter.convert(id)
        // )
        id
      )
      .query(Y.class).single();
  }
}
