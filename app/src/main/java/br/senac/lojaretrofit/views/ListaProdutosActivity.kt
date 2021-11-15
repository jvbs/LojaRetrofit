package br.senac.lojaretrofit.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.lojaretrofit.databinding.ActivityListaProdutosBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import br.senac.lojaretrofit.services.ProdutoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.senac.lojaretrofit.models.Produto
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import br.senac.lojaretrofit.databinding.CardItemBinding
import br.senac.lojaretrofit.services.API
import com.squareup.picasso.Picasso

class ListaProdutosActivity : AppCompatActivity() {
    lateinit var binding: ActivityListaProdutosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        atualizarProdutos()
    }

    fun atualizarProdutos() {
        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if(response.isSuccessful) {
                    binding.progressBar.visibility = View.GONE

                    val listaProdutos = response.body()
                    atualizarUI(listaProdutos)
                } else {
                    // val error = response.errorBody().toString()
                    Snackbar.make(binding.container, "Não é possível atualizar os produtos.",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE

                Snackbar.make(binding.container, "Não é possível atualizar os produtos.",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar o serviço.", t)
            }
        }

        API.produto.listar().enqueue(callback)

        binding.progressBar.visibility = View.VISIBLE

    }

    fun atualizarUI(lista: List<Produto>?) {
        binding.container.removeAllViews()

        lista?.forEach {
            val cardBinding = CardItemBinding.inflate(layoutInflater)

            cardBinding.txtName.text = it.nomeProduto
            cardBinding.txtPrice.text = it.descProduto

            Picasso
                .get()
                .load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/${it.idProduto}")
                .into(cardBinding.imageView)

            binding.container.addView(cardBinding.root)
        }
    }

}