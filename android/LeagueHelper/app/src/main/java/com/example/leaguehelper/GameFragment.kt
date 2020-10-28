package com.example.leaguehelper

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.leaguehelper.model.Summoner
import com.example.leaguehelper.model.SummonerSpell
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {

    private lateinit var gameDataViewModel: GameDataViewModel
    private val champBaseUrl = "http://ddragon.leagueoflegends.com/cdn/10.21.1/img/champion/"
    private val spellBaseUrl = "http://ddragon.leagueoflegends.com/cdn/10.21.1/img/spell/"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDataViewModel = activity?.run {
            ViewModelProviders.of(this).get(GameDataViewModel::class.java)
        }!!
        gameDataViewModel?.data?.observe(this, Observer {
            val summoners = gameDataViewModel.data.value
            setUpImageViews(summoners)
        })
    }

    private fun setUpImageViews(summoners: MutableList<Summoner>?) {
        if (summoners == null) return
        insertImages(champ_0, champ_0_spell1, champ_0_spell2, summoners[0].getSpell1Image(), summoners[0].getSpell2Image(), summoners[0].getChampImage())
        insertImages(champ_1, champ_1_spell1, champ_1_spell2, summoners[1].getSpell1Image(), summoners[1].getSpell2Image(), summoners[1].getChampImage())
        insertImages(champ_2, champ_2_spell1, champ_2_spell2, summoners[2].getSpell1Image(), summoners[2].getSpell2Image(), summoners[2].getChampImage())
        insertImages(champ_3, champ_3_spell1, champ_3_spell2, summoners[3].getSpell1Image(), summoners[3].getSpell2Image(), summoners[3].getChampImage())
        insertImages(champ_4, champ_4_spell1, champ_4_spell2, summoners[4].getSpell1Image(), summoners[4].getSpell2Image(), summoners[4].getChampImage())

        setUpImageClicks(champ_0_spell1, champ_0_spell1_tv, summoners[0].getSpell1())
        setUpImageClicks(champ_0_spell2, champ_0_spell2_tv, summoners[0].getSpell2())
        setUpImageClicks(champ_1_spell1, champ_1_spell1_tv, summoners[1].getSpell1())
        setUpImageClicks(champ_1_spell2, champ_1_spell2_tv, summoners[1].getSpell2())
        setUpImageClicks(champ_2_spell1, champ_2_spell1_tv, summoners[2].getSpell1())
        setUpImageClicks(champ_2_spell2, champ_2_spell2_tv, summoners[2].getSpell2())
        setUpImageClicks(champ_3_spell1, champ_3_spell1_tv, summoners[3].getSpell1())
        setUpImageClicks(champ_3_spell2, champ_3_spell2_tv, summoners[3].getSpell2())
        setUpImageClicks(champ_4_spell1, champ_4_spell1_tv, summoners[4].getSpell1())
        setUpImageClicks(champ_4_spell2, champ_4_spell2_tv, summoners[4].getSpell2())
    }

    private fun insertImages(champIV: ImageView, spell1IV: ImageView, spell2IV: ImageView, spell1URL: String, spell2URL: String, champURL: String) {
        Picasso.get().load(spell1URL).into(spell1IV)
        Picasso.get().load(spell2URL).into(spell2IV)
        Picasso.get().load(champURL).into(champIV)
    }

    private fun setUpImageClicks(imageView: ImageView, textView: TextView, spell: SummonerSpell) {
        val milli = (spell.cooldown * 1000).toLong()
        imageView.setOnClickListener {
            imageView.alpha = 0.3F
            val timer = object: CountDownTimer(milli, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    textView.text = (millisUntilFinished / 1000).toString()
                }
                override fun onFinish() {
                    imageView.alpha = 1F
                    textView.text = ""
                }
            }
            timer.start()
        }
    }

}
