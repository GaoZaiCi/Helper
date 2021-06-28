
var btn
var botton = false
var botton1 = false
var openwindow = null
var openwindow1 = null
var blood
var pointent = -1
var time = -1
var typeblood = new Array()
typeblood[10] = 4
typeblood[11] = 10
typeblood[12] = 10
typeblood[13] = 8
typeblood[14] = 10
typeblood[15] = 20
typeblood[16] = 10
typeblood[18] = 3
typeblood[32] = 20
typeblood[33] = 20
typeblood[34] = 20
typeblood[35] = 16
typeblood[36] = 20
typeblood[37] = 10
typeblood[38] = 40
typeblood[39] = 8

typeblood[17] = 10
typeblood[19] = 6
typeblood[20] = 100
typeblood[21] = 10
typeblood[22] = 10
typeblood[40] = 12
typeblood[41] = 10
typeblood[42] = 10
typeblood[43] = 20
typeblood[44] = 20
typeblood[45] = 26
typeblood[63] = 20

typeblood[46]=20
typeblood[47]=20
typeblood[23]=10
typeblood[26]=10
typeblood[27]=10
typeblood[25]=10
typeblood[24]=10

typeblood[55]=8
typeblood[54]=30
typeblood[28]=30

typeblood[49]=30
typeblood[50]=80

typeblood[104]=24
typeblood[57]=24
typeblood[29]=15
typeblood[105]=14

var lockent = -1
var enable_script_var = 1;

function modTick() {
	if(enable_script_var == 0 )
	{
		return;
	}
	
	if (botton && !botton1) {
		botton1 = true
		/*
		var ctx = com.mojang.minecraftpe.MainActivity.currentMainActivity.get()
		ctx.runOnUiThread(new java.lang.Runnable({
			run : function() {
				try {
					var btn = new android.widget.Button(ctx);
					btn.setText("♥")
					var lay = new android.widget.LinearLayout(ctx)
					lay.addView(btn)
					btn.setOnTouchListener(new android.view.View.OnTouchListener()) { onTouch: function(v,e) {
							if (e.getAction() != 1) {
								time = -2
								var x = (e.getX() / 1 + dip2px(ctx, 10)) / (dip2px(ctx, 180)) + 1
								var ent
								if (pointent != -1 && lockent == -1)
									ent = pointent
								else if (pointent == -1 && lockent != -1)
									ent = lockent
								if (x < 1.2)
									Entity.setHealth(ent, x * typeblood[Entity.getEntityTypeId(ent)])
								else
									Entity.setHealth(ent, Math.round(typeblood[Entity.getEntityTypeId(ent)] * 1.2 + (x - 1.2) * 100))
							} else {
								time = -1
							}
							return true
						}
					});
					openwindow1 = new android.widget.PopupWindow(lay, dip2px(ctx, 30), dip2px(ctx, 30));
					openwindow1.showAtLocation(ctx.getWindow().getDecorView(), android.view.Gravity.TOP | android.view.Gravity.RIGHT, dip2px(ctx, 350), ctx.getWindowManager().getDefaultDisplay().getHeight() * 0.19);
				} catch(err) {
					print(err);
				}
			}
		}))
		*/
	}
	if (botton1 && !botton) {
		botton1 = false
		var ctx = com.mojang.minecraftpe.MainActivity.currentMainActivity.get()
		ctx.runOnUiThread(new java.lang.Runnable({
			run : function() {
				try {
					if(openwindow1){
						openwindow1.dismiss();
						openwindow1 = null;
					}
				} catch(err) {
					print(err);
				}
			}
		}))
	}
	if (Player.getPointedEntity() != -1)
		lockent = -1
	
	var typeId = Entity.getEntityTypeId(Player.getPointedEntity());
	if ((Player.getPointedEntity() != -1 && (typeId < 64 || typeId>=104) && (pointent != Player.getPointedEntity() | blood != Entity.getHealth(Player.getPointedEntity())) ) | (lockent != -1 && Entity.getHealth(lockent) != blood && Player.getPointedEntity() == -1)) {
		if (lockent == -1)
			blood = Entity.getHealth(Player.getPointedEntity())
		else
			blood = Entity.getHealth(lockent)
		pointent = Player.getPointedEntity()
		time = -1
		var ctx = com.mojang.minecraftpe.MainActivity.currentMainActivity.get()
		ctx.runOnUiThread(new java.lang.Runnable({
			run : function() {
				try {
					var bmp = android.graphics.Bitmap.createBitmap(dip2px(ctx, 180), dip2px(ctx, 25), android.graphics.Bitmap.Config.ARGB_8888);
					var pa = new android.graphics.Paint()
					pa.setStyle(android.graphics.Paint.Style.STROKE)
					var cv = new android.graphics.Canvas();
					cv.setBitmap(bmp);
					pa.setColor(android.graphics.Color.WHITE)
					pa.setStrokeWidth(dip2px(ctx, 4))
					cv.drawRect(0, 0, dip2px(ctx, 180), dip2px(ctx, 25), pa)
					pa.setStyle(android.graphics.Paint.Style.FILL)
					pa.setColor(android.graphics.Color.RED)
					if (lockent == -1)
						cv.drawRect(dip2px(ctx, 5), dip2px(ctx, 5), dip2px(ctx, 5 / 1 + blood / typeblood[Entity.getEntityTypeId(pointent)] * 170), dip2px(ctx, 20), pa)
					else
						cv.drawRect(dip2px(ctx, 5), dip2px(ctx, 5), dip2px(ctx, 5 / 1 + Entity.getHealth(lockent) / typeblood[Entity.getEntityTypeId(lockent)] * 170), dip2px(ctx, 20), pa)
					var bv = new android.widget.ImageView(ctx)
					bv.setImageBitmap(bmp)
					var lay = new android.widget.RelativeLayout(ctx)
					lay.addView(bv)
					/*lay.setOnTouchListener(new android.view.View.OnTouchListener() {
					 onTouch: function(v,e){
					 if(e.getAction()!=1){
					 time=-2
					 var x=(e.getX()/1+dip2px(ctx,10))/(dip2px(ctx,180))
					 if(pointent!=-1)  Entity.setHealth(pointent,x*typeblood[Entity.getEntityTypeId(pointent)])
					 else  Entity.setHealth(lockent,x*typeblood[Entity.getEntityTypeId(lockent)])
					 }
					 else{
					 time=-1
					 }
					 return true
					 }});
					 */
					var t = new android.widget.TextView(ctx)
					var textParams = new android.widget.LinearLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT)

					t.setTextSize(16)
					
					var total = typeblood[Entity.getEntityTypeId(pointent)]
					if(total!=null && total!=org.mozilla.javascript.Undefined){
						if (lockent == -1)
							t.setText(" " + blood + "/" + typeblood[Entity.getEntityTypeId(pointent)])
						else
							t.setText(" " + Entity.getHealth(lockent) + "/" + typeblood[Entity.getEntityTypeId(lockent)])
						t.setLayoutParams(textParams)
						lay.addView(t)
					
						if (openwindow != null)
							openwindow.dismiss()
						openwindow = null
						openwindow = new android.widget.PopupWindow(lay, dip2px(ctx, 180), dip2px(ctx, 25));
						openwindow.showAtLocation(ctx.getWindow().getDecorView(), android.view.Gravity.TOP | android.view.Gravity.RIGHT, dip2px(ctx, 380), ctx.getWindowManager().getDefaultDisplay().getHeight() * 0.2);
						botton = true
					}
				} catch(err) {
					java.lang.System.out.println(err);
				}
			}
		}));
	}
	if (Player.getPointedEntity() == -1 && (pointent != -1 | lockent != -1) && time == -1) {
		time = 40
		if (pointent != -1)
			lockent = pointent
		pointent = -1;
	}
	if (time == 0) {
		botton = false
		lockent = -1
		pointent = -1
		time = -1
		var ctx = com.mojang.minecraftpe.MainActivity.currentMainActivity.get()
		ctx.runOnUiThread(new java.lang.Runnable({
			run : function() {
				try {
					if (openwindow != null)openwindow.dismiss();
					openwindow = null
				} catch(err) {
					print(err);
				}
			}
		}))
	}
	if (time > 0)
		time--
}

function dip2px(ctx, dips) {
	return Math.ceil(aaaa * dips);
}

var ctx = com.mojang.minecraftpe.MainActivity.currentMainActivity.get()
var aaaa = ctx.getResources().getDisplayMetrics().density
function leaveGame() {
	if (openwindow != null) {
		var ctx = com.mojang.minecraftpe.MainActivity.currentMainActivity.get()
		ctx.runOnUiThread(new java.lang.Runnable({
			run : function() {
				try {
					openwindow.dismiss()
					botton = false
					openwindow = null
					pointent = -1
					time = -1
					if (openwindow1){
						openwindow1.dismiss()
						openwindow1 = null
					}
					
				} catch(err) {
					print(err);
				}
			}
		}))
	}
}

function enable_script_func() {
	enable_script_var = 1;
}

function disable_script_func() {
//	leaveGame();
//	enable_script_var = 0;
}
