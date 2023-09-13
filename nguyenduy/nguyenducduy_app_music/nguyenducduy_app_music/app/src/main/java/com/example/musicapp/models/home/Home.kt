package com.example.musicapp.models.home

class Home (
    var listenToday : ArrayList<Song> = arrayListOf(),
    val newSong : ArrayList<Song> = arrayListOf(),
    val newAlbum : ArrayList<Song> = arrayListOf(),
    val songVietNam : ArrayList<Song> = arrayListOf(),
){
    companion object{
        fun getHome():Home{
            val home = Home()
            val ls1 = Song("https://cdn.popsww.com/blog/sites/2/2022/03/nhac-tik-tok-1280x720.jpg","Nhạc TikTok","")
            val ls2 = Song("https://avatar-ex-swe.nixcdn.com/playlist/2017/11/15/3/7/9/d/1510734406972_500.jpg","Nhạc Phim Hoa Ngữ","")
            val ls3 = Song("https://i.ytimg.com/vi/CulBRA4HFgk/maxresdefault.jpg","Rap Việt","")
            home.listenToday.add(ls1)
            home.listenToday.add(ls2)
            home.listenToday.add(ls3)

            val s1 = Song("https://i.ytimg.com/vi/W4P8gl4dnrg/hqdefault.jpg","Hẹn Ước Từ Hư Vô","Mỹ Tâm")
            val s2 = Song("https://o.rada.vn/data/image/2022/03/29/Khi-nao-700.jpg","Khi Nào","Châu Dương")
            val s3 = Song("https://vnw-img-cdn.popsww.com/api/v2/containers/file2/cms_thumbnails/van_nho_tuan_hung-4396352cfdcd-1570189115564-mbah7VOX.jpg?v=0&maxW=640","Vẫn Nhớ","Tuấn Hưng")
            home.newSong.add(s1)
            home.newSong.add(s2)
            home.newSong.add(s3)

            val al1 = Song("https://wifeless-superlativ.000webhostapp.com/HinhAnh/TheLoai/anh39.jpg","Still Live","BigBang")
            val al2 = Song("https://wifeless-superlativ.000webhostapp.com/HinhAnh/TheLoai/anh40.jpeg","So far Away","Gui")
            val al3 = Song("https://wifeless-superlativ.000webhostapp.com/HinhAnh/TheLoai/anh10.jpeg","Tom Boy","GLIDE")
            home.newAlbum.add(al1)
            home.newAlbum.add(al2)
            home.newAlbum.add(al3)

            val vn1 = Song("https://i.ytimg.com/vi/qkPgUgkQE4Y/maxresdefault.jpg","Đế Vương","")
            val vn2 = Song("https://i.ytimg.com/vi/EBpp2VTSI2Q/maxresdefault.jpg","Yêu Đương Khó Quá","")
            val vn3 = Song("https://i.ytimg.com/vi/Fwh6RMq9Sz4/maxresdefault.jpg","Hoa Tàn Tình Tan","")
            home.songVietNam.add(vn1)
            home.songVietNam.add(vn2)
            home.songVietNam.add(vn3)

            return home
        }
    }
}
class Song(
    val image : String = "",
    val title : String = "",
    val subTitle : String = ""
){

}