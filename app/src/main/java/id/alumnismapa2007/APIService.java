package id.alumnismapa2007;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by TOSHIBA on 30/04/2017.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("insert.php")
    Call<Value> insert(@Field("nama") String nama,
                       @Field("nomor") String nomor,
                         @Field("kelas") String kelas,
                         @Field("sekolah") int sekolah,
                         @Field("rumah") int rumah,
                         @Field("lain") int lain,
                         @Field("tgl27") int tgl27,
                         @Field("tgl28") int tgl28,
                         @Field("tgl29") int tgl29,
                         @Field("tgl30") int tgl30,
                         @Field("tgl1") int tgl1,
                         @Field("tgl2018") int tgl2018,
                         @Field("budget") String budget,
                         @Field("keterangan") String keterangan);

    @GET("view.php")
    Call<Value> view();

    @FormUrlEncoded
    @POST("viewKelas.php")
    Call<Value> lihatKelas(@Field("kelas") String kelas);
}
