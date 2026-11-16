package com.ghostmapper.pro

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  val root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(32,48,32,24); setBackgroundColor(Color.rgb(8,17,29)) }
  fun label(s:String) = TextView(this).apply { text=s; textSize=18f; setTextColor(Color.CYAN); setPadding(0,10,0,10) }
  root.addView(label("GHOSTMAPPER PRO  •  ANDROID"))
  root.addView(label("Mapeamento de teclas para toques — versão inicial"))
  fun button(s:String, action:()->Unit) { root.addView(Button(this).apply { text=s; setOnClickListener { action() } }) }
  button("1. Autorizar sobreposição") { startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) }
  button("2. Ativar serviço de acessibilidade") { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
  button("3. Abrir editor flutuante") { if (Settings.canDrawOverlays(this)) MapperService.instance?.showEditor() ?: Toast.makeText(this,"Ative a acessibilidade primeiro",Toast.LENGTH_LONG).show() else Toast.makeText(this,"Autorize a sobreposição",Toast.LENGTH_LONG).show() }
  root.addView(label("No editor: escolha a tecla, posição X/Y em porcentagem da tela e toque em Salvar. Exemplo: tecla J → toque em 80%, 70%."))
  root.addView(label("Limitações: mapeia teclas físicas; mouse relativo, analógicos, mira FPS e compatibilidade universal ainda não implementados."))
  setContentView(ScrollView(this).apply { addView(root) })
 }
}
