package com.ghostmapper.pro

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Color
import android.content.Context
import android.view.*
import android.widget.*
import org.json.JSONObject

class MapperService : AccessibilityService() {
 companion object { var instance: MapperService? = null }
 private var wm: WindowManager? = null
 private var overlay: View? = null
 private val mappings = mutableMapOf<Int,Pair<Float,Float>>()
 override fun onServiceConnected() { super.onServiceConnected(); instance=this; wm=getSystemService(Context.WINDOW_SERVICE) as WindowManager; load() }
 override fun onDestroy() { hideEditor(); instance=null; super.onDestroy() }
 override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) {}
 override fun onInterrupt() {}
 override fun onKeyEvent(event: android.view.KeyEvent): Boolean {
  if (event.action != KeyEvent.ACTION_DOWN) return false
  val target=mappings[event.keyCode] ?: return false
  val metrics=resources.displayMetrics
  val path=Path().apply { moveTo(metrics.widthPixels*target.first,metrics.heightPixels*target.second) }
  val stroke=GestureDescription.StrokeDescription(path,0,65)
  dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(),null,null)
  return true
 }
 private fun load() { mappings.clear(); val prefs=getSharedPreferences("maps",MODE_PRIVATE); val data=JSONObject(prefs.getString("keys","{}") ?: "{}"); for (key in data.keys()) { val point=data.getJSONArray(key); mappings[key.toInt()]=Pair(point.getDouble(0).toFloat(),point.getDouble(1).toFloat()) } }
 private fun save() { val data=JSONObject(); mappings.forEach { (key,point)-> data.put(key,org.json.JSONArray().put(point.first).put(point.second)) }; getSharedPreferences("maps",MODE_PRIVATE).edit().putString("keys",data.toString()).apply() }
 fun showEditor() {
  if (overlay!=null) return
  val box=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(20,20,20,20); setBackgroundColor(Color.rgb(13,36,58)) }
  fun edit(hint:String)=EditText(this).apply { setSingleLine(true); this.hint=hint; setTextColor(Color.WHITE); setHintTextColor(Color.LTGRAY); box.addView(this) }
  box.addView(TextView(this).apply { text="GHOSTMAPPER • EDITOR"; textSize=19f; setTextColor(Color.CYAN) })
  val key=edit("Tecla (ex: J)"); val x=edit("X % (ex: 80)"); val y=edit("Y % (ex: 70)")
  box.addView(Button(this).apply { text="SALVAR MAPEAMENTO"; setOnClickListener {
   val code=KeyEvent.keyCodeFromString("KEYCODE_"+key.text.toString().uppercase().trim())
   val px=x.text.toString().toFloatOrNull(); val py=y.text.toString().toFloatOrNull()
   if (code==KeyEvent.KEYCODE_UNKNOWN || px==null || py==null || px !in 0f..100f || py !in 0f..100f) Toast.makeText(this@MapperService,"Informe tecla válida e X/Y entre 0 e 100",Toast.LENGTH_LONG).show()
   else { mappings[code]=Pair(px/100f,py/100f); save(); Toast.makeText(this@MapperService,"Mapeamento salvo",Toast.LENGTH_SHORT).show() }
  } })
  box.addView(Button(this).apply { text="FECHAR EDITOR"; setOnClickListener { hideEditor() } })
  val params=WindowManager.LayoutParams(620,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT).apply { gravity=Gravity.TOP or Gravity.CENTER_HORIZONTAL; y=100 }
  // The editor must accept keyboard input, so remove NOT_FOCUSABLE.
  params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
  try { wm?.addView(box,params); overlay=box } catch (_:Exception) { Toast.makeText(this,"Não foi possível abrir sobreposição",Toast.LENGTH_LONG).show() }
 }
 fun hideEditor() { overlay?.let { try { wm?.removeView(it) } catch (_:Exception) {} }; overlay=null }
}
