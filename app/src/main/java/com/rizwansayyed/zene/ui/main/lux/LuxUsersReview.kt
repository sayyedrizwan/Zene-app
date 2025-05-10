package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.lux.billing.ReviewData
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal


val reviews = listOf(
    ReviewData(
        "Sofia Morales",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ60UJlW_npR7Yp303WwhcXoQbc6JqTHYD7ZgKd6uCxLx3qTPDP5HZxucCEUDD-2AH9vEI&usqp=CAU",
        "Finally found an app that lets me enjoy music and podcasts without ads \uD83D\uDE0C\uD83C\uDFB6. Zene Luxe is insanely affordable too!"
    ),
    ReviewData(
        "Kenji Takahashi",
        "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEi9Xf5gz5nXQn16UpEuDF14jYd_MFqUJtrAgkuo92A53E5AmqZVpGQzFokRrvOIoxTv84JnjsaIh1pQUEKz0TsNgdHS0WxeOCfQC3GL69otTxYza90bloMSb1pS5Mq0Cs7wm_wjrKvTySaN/s1600/PatDiamondHead23June08+for+blog.jpg",
        "Switched from Spotify and haven’t looked back. Zene gives me free access to podcasts, music, AND radio — all ad-free \uD83D\uDE0D\uD83D\uDCFB\uD83D\uDD25."
    ),
    ReviewData(
        "Aarav Mehta",
        "https://photos.smugmug.com/Portfolios/Male/Aarav-Mehta/i-rpq4xhg/4/MqhXk6BkzWCvsNNftg25Sn5cc8wjWMhQHwn68bJBR/L/%C2%A9_www.ileshshah.photography--12-L.jpg",
        "Best decision ever! For the price of a coffee ☕ I get endless songs, chill podcasts, and no annoying interruptions \uD83D\uDCAF."
    ),
    ReviewData(
        "Mina Jovanović",
        "https://www.fin.kg.ac.rs/images/Fakultet/Osoblje/Katedra_za_elektrotehniku/mina_vaskovic_jovanovic.jpg",
        "I honestly thought it was too good to be true. No ads, fresh music, and smooth radio stations — all at a price no one can beat \uD83E\uDD11\uD83C\uDFA7."
    ),
    ReviewData(
        "Layla Al-Fulan",
        "https://64.media.tumblr.com/01567a0fa35aca282a3895c3d4f9f3b4/tumblr_n97ckhZSa61trnp1xo1_500.png",
        "Zene Luxe is perfect for students like me \uD83C\uDF93. Cheap, clean UI, and I can vibe without a single ad \uD83E\uDD19\uD83C\uDFB5. \n"
    ),
    ReviewData(
        "Mateo Rodríguez",
        "https://i.pinimg.com/736x/e1/02/65/e10265c619ad99bfa0e43106c7127cab.jpg",
        "Love discovering new indie music and podcasts here — without any ads! The radio stations are \uD83D\uDD25 and the price is unmatched \uD83D\uDC4C."
    ),
    ReviewData(
        "Luca Bianchi",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSB8XobfgR3w7d-y9aCSz-kz8gCcerCEjHrg&s",
        "I use Zene Premium every day during my commute \uD83D\uDE97. Podcasts, radio, music — all in one place and completely ad-free ✨."
    ),
    ReviewData(
        "Arjun Patel", "https://arjunppatel.github.io/assets/img/face.png",
        "Way cheaper than Spotify or YouTube Music and does exactly what I need: non-stop audio with no ads \uD83D\uDE4C\uD83C\uDFB6. \n"
    ),
    ReviewData(
        "Noor El-Sayed",
        "https://instagram.fbom28-1.fna.fbcdn.net/v/t51.29350-15/379695119_631592579123605_7606114879995809309_n.heic?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6IkNBUk9VU0VMX0lURU0uaW1hZ2VfdXJsZ2VuLjE0NDB4MTgwMC5zZHIuZjI5MzUwLmRlZmF1bHRfaW1hZ2UifQ&_nc_ht=instagram.fbom28-1.fna.fbcdn.net&_nc_cat=101&_nc_oc=Q6cZ2QGjyfX-C1kXpDPCagndpvHeFOG_EkxInzcddVVjE1f0UAAtystuvYe81gn_j5zB9GE&_nc_ohc=8sc_rBwQU_4Q7kNvwEWo24S&_nc_gid=cnrVa442voeCZaNIxmu4gA&edm=APs17CUBAAAA&ccb=7-5&ig_cache_key=MzE5MzE5Mzk0MzY0MDgwMTQyMw%3D%3D.3-ccb7-5&oh=00_AfICcJ0sJScmsEkC4NFpCAaqVozs95cvfL_Mr8SKr5jwBw&oe=68258AC8&_nc_sid=10d13b",
        "I didn’t expect much for the price, but Zene Premium blew me away. Free music, good podcasts, smooth radio, and peace of mind \uD83D\uDE0A\uD83D\uDCF1."
    ),
    ReviewData(
        "Elio Dimitriou",
        "https://media.licdn.com/dms/image/v2/D4D03AQE8fvAVIk1thg/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689163494538?e=2147483647&v=beta&t=2sdn7wh0m-Qv16pDcl6pbS3_Tovfgddk4gCNZLRj1Ig",
        "If you're tired of ads and high subscription prices, Zene Premium is the way to go \uD83D\uDCB8\uD83C\uDFA7. It's simple, solid, and super affordable."
    ),
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LuxUsersReview() {
    TextViewBold(stringResource(R.string.what_our_luxe_users_say), 19)
    Spacer(Modifier.height(8.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reviews) { review ->
            Card(
                modifier = Modifier
                    .width(300.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        GlideImage(
                            review.photo, review.name,
                            Modifier
                                .padding(horizontal = 10.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(100)),
                            contentScale = ContentScale.Crop
                        )
                        TextViewBold(review.name, 15)
                    }
                    Spacer(Modifier.height(10.dp))
                    TextViewNormal(review.review, 15)
                }
            }
        }
    }
}