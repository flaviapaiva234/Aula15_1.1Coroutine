package com.example.aula15_11funcionando

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aula15_11funcionando.databinding.ActivityMainBinding
import kotlinx.coroutines.*

// esse app da erro pq fica com um loop infinito na thread principal, fazerdo app travar

class MainActivity : AppCompatActivity() {
    //1.
    private lateinit var binding: ActivityMainBinding

    private var number: Int = 0
    private var isLoopStarted: Boolean = false // vai dizer se esse loop vai acontecer ou não

    //criar uma variável do tipo myJob para manipular diretamente essa coroutine
    //esse job representa essa fila em segundo plano onde está acontecendo os evento, quando se tem esse objeto em mãos, então consegue controlar o coroutine
    private lateinit var myJob: Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        clickEvents() // chamando a função para lidar com os clicks dos eventos
    }
    // vai ter um loop, e enquanto ele vai rodando, vai incrementando uma variável e vai imprimir na minha tela
    //assim que apertar um dos botões vai começar isso

    //criar uma função que vai lidar com o click de cada função
    private fun clickEvents(){
        binding.apply{
            //assim que clicar no botão de iniciar, o loop será iniciado
            btnStart.setOnClickListener {
 //               isLoopStarted = true //vai iniciar o loop
                initLoop()
            }

            // esse botão seria para fazer parar o loop
            btnEnd.setOnClickListener {
 //               isLoopStarted = false // se o loop der false, para a contagem quando for clicado
                  myJob.cancel() // cancela o processo iniciado
            }

        }

    }

    //2. vai iniciar o loop
    private fun initLoop(){
        //Esse coroutine vai determinar se vai ser usado a thread principal ou a thread em segundo plano
        //para ser usada em seguno plano tem que colocar esse contexto (Dispatchers.IO), que é algo que sempre acontece em segundo plano, e essa fila tem que ser inicializada com um launch e coloca esse while dentro desse launch
        // esse coroutine retorna um objeto do tipo Job, que através dele é possível comtrolar o cancelamento de processos em segundo plano
        myJob = CoroutineScope(Dispatchers.IO).launch {
        while (true) {             //Loop ocorrendo em segundo plano
            number++             // a cada interação desse laço vai ser incrementado um number
            delay(1000)                 // se eu quiser imcrementar um certo tempo entre uma incrementação e outra é só usar o loop, se eu quiser que vá bem rápido é só tirar o delay, o delay pausa momentaniamente, e o tempo é sempre em milisegundos
            //para resgatar a thread de while, pego essa atividade em segundo plano, ela vai produzir um resultado que seria o number, pego o resultado da fila em segundo plano e levo para a fila em primeiro plano, só o resultado
            // esse with leva o resultado para a thread de UI
            withContext(Dispatchers.Main){
                binding.textView.text = "$number"            // e mostrar na minha tela o valor desse número incrementado
                Log.i(ContentValues.TAG, " Contagem: " + number) // esse Lop.i é tipo um print que vai imprimir resultados no painel RUN (na parte inferior do android Studio), o restante é só para fazer essa impressão funcionar
            }

        }
        }
    }
}

// sempre que estiver fazendo um aplicativo com um while ou tipo uma capitura de banco de dados ou de um servidor, tudo isso tem que acontecer em threads de segundo plano,
// pq se não, o aplicativo vai ficar congelando
//da para ter controle total do que está em segundo plano