import java.util.ArrayDeque
//переменные не могут быть пустой строкой, начинаться с цифры и должны содержать символы лат. алфавита, цифры, символы "$", "_"
fun Boolean.toInt() = if (this) 1 else 0;
fun Boolean.toFloat() = if (this) 1f else 0f;
fun Int.toBool() = if (this==0) false else true;
fun Float.toBool() = if (this==0f) false else true;
fun isCorrect(NAME: String): Boolean
{
    if (NAME.length==0) return false;
    if (NAME[0].isDigit()) return false;
    return NAME.matches(Regex("[a-zA-Z0-9_$]+"));
}

fun isInt(x: String): Boolean {
    return x.matches(Regex("[0-9]+"));
}

fun isFloat(x: String): Boolean {
    return x.matches(Regex("[0-9]+\\.[0-9]+"));
}

//ПАРСЕР НАЧАЛО
fun is_op (c: Char): Boolean {
    val s = arrayOf('+', '-', '*', '/', '%', '|', '&', '!', '<', '>', '⩽', '⩾', '=', '≠')
	return (c in s);
}


fun priority (ID: Int): Int {
	if (ID < 0) {
    	if ((-ID).toChar() == '!') return 3;
		return 100; // op == -'+' || op == -'-'
    }
    var op = ID.toChar()
    if (op == '|') return 1;
    if (op == '&') return 2;
    if (op=='>' || op=='<' || op=='⩽' || op=='⩾' || op=='=' || op=='≠') return 4;
	if (op == '+' || op == '-') return 10;
    if (op == '*' || op == '/' || op == '%') return 20;
	return -1;
}

fun process_op (st: ArrayDeque<String>, ID: Int): ArrayDeque<String> {
    var op: Char;
	if (ID < 0) {
        if (st.count() == 0) return st;
		var l_str = st.last();
        st.removeLast();
        op = (-ID).toChar()
        if (isInt(l_str)) {
            var l = l_str.toInt();
			if (op=='+')  st.addLast(l.toString());
			if (op=='-')  st.addLast((-l).toString());
        	if (op=='!')  {var v = l.toBool(); v = !v; st.addLast(v.toString())};
        }
        else if (isFloat(l_str)) {
            var l = l_str.toFloat();
			if (op=='+')  st.addLast(l.toString());
			if (op=='-')  st.addLast((-l).toString());
        	if (op=='!')  {var v = l.toBool(); v = !v; st.addLast(v.toString())};
        }
        else {
            st.clear();
            return st;
        }
	}
	else {
        if (st.count() < 2) {
            st.clear();
            return st;
        }
		var r_str = st.last();
        st.removeLast();
		var l_str = st.last();
        st.removeLast();
        op = ID.toChar()
        if ((!isFloat(r_str) && !isInt(r_str)) || (!isFloat(l_str) && !isInt(l_str))) {
            st.clear();
            return st;
        }
        if (!isFloat(r_str) && !isFloat(l_str)) {
            var l=l_str.toInt();
            var r=r_str.toInt();
            var res: Int = 0;
            when(op) {
                '+' ->  res = (l + r); 
                '-' ->  res = (l - r);  
                '*' ->  res = (l * r);
                '/' ->  res = (l / r);
                '%' ->  res = (l % r);
                '|' ->  res = ((l.toBool() || r.toBool()).toInt());
                '&' ->  res = ((l.toBool() && r.toBool()).toInt());
                '<' ->  res = ((l < r).toInt());
                '>' ->  res = ((l > r).toInt());
                '=' ->  res = ((l == r).toInt());
                '⩽' ->  res = ((l <= r).toInt());
                '⩾' ->  res = ((l >= r).toInt());
                '≠' -> res = ((l != r).toInt());
            }
            st.addLast(res.toString());
        }
        else {
            var l=l_str.toFloat();
            var r=r_str.toFloat();
            var res = 0f;
            when(op) {
                '+' ->  {res = (l + r); st.addLast(res.toString())}
                '-' ->  {res = (l - r); st.addLast(res.toString())}  
                '*' ->  {res = (l * r); st.addLast(res.toString())}
                '/' ->  {res = (l / r); st.addLast(res.toString())}
                '%' ->  {st.clear(); return st;}
                '|' ->  {st.addLast((l.toBool() || r.toBool()).toInt().toString());}
                '&' ->  {st.addLast((l.toBool() && r.toBool()).toInt().toString());}
                '<' ->  {st.addLast((l < r).toInt().toString());}
                '>' ->  {st.addLast((l > r).toInt().toString());}
                '=' ->  {st.addLast((l == r).toInt().toString());}
                '⩽' ->  {st.addLast((l <= r).toInt().toString());}
                '⩾' ->  {st.addLast((l >= r).toInt().toString());}
                '≠' -> {st.addLast((l != r).toInt().toString());}
            }
            
        }
	}
    return st;
}

fun calc (str: String): String {
	
    var EXPR = str;
    
    var may_unary = true;
    var s = EXPR.replace("\\s".toRegex(), "");
    s = s.replace("\\&\\&".toRegex(), "&");
    s = s.replace("\\|\\|".toRegex(), "|");
    s = s.replace("<=".toRegex(), "⩽");
    s = s.replace(">=".toRegex(), "⩾");
    s = s.replace("==".toRegex(), "=");
    s = s.replace("!=".toRegex(), "≠");
    s = s.replace("true".toRegex(), "1");
    s = s.replace("false".toRegex(), "0");
    var st = ArrayDeque<String>()
    var op = ArrayDeque<Int>()
    
    var i = 0;
	while (i <s.length) {
			if (s[i] == '(') {
				op.addLast('('.toInt());
				may_unary = true;
			}
			else if (s[i] == ')') {
				while (op.last() != '('.toInt()) {
					st = process_op(st, op.last());
                	op.removeLast();
                }
				op.removeLast();
				may_unary = false;
			}
			else if (is_op (s[i])) {
                
				var curop = s[i].toInt();
				if (may_unary) {
                    if (s[i]!='-' && s[i]!='+' && s[i] != '!') return "error";
             		curop = -curop;
                    
                } 
				while (!op.isEmpty() && (
					curop >= 0 && priority(op.last()) >= priority(curop)
					|| curop < 0 && priority(op.last()) > priority(curop))) {
					st = process_op(st, op.last())
                    op.removeLast();
                }
				op.addLast(curop);
				may_unary = true;
			}
			else {
				var OPER = "";
				while (i < s.length && !is_op(s[i]) && s[i] != '(' && s[i] != ')') {
                    val ch = s[i++]
					OPER+=ch;
                }
				--i;
                if (OPER[0].isDigit()) st.addLast(OPER)
                else { 
                    if (isDefined(OPER) && (getType(OPER)=="Int" || getType(OPER)=="Bool")) {
                        st.addLast(getValue(OPER))
                    }
                    else return "error";
                }
				may_unary = false;
			}
            i++
    }
	while (!op.isEmpty()) {
		st = process_op(st, op.last());
        op.removeLast();
    }
    
    if (st.isEmpty()) return "error";
	return st.last().toString();
}

fun isTrue(x: String): Boolean {
    var r = calc(x);
    if (r == "0" || r == "error") return false;
    return true;
}

//ПАРСЕР КОНЕЦ

//КЛАССЫ
var map = mutableMapOf<String, Variable>()

abstract class Variable(vName: String) {
    val varName = vName;
    var defined = false;
    abstract var value: Array<String>;
    abstract var Type: String;
    init {
       map.put(vName, this)
    }
}


class IntClass(varName: String) : Variable(varName) {
	override var Type = "Int";
 	override var value = arrayOf("0");
}

class FloatClass(varName: String) : Variable(varName) {
	override var Type = "Float";
 	override var value = arrayOf("0.0");
}


class BoolClass(varName: String) : Variable(varName) {
	override var Type = "Bool";
 	override var value = arrayOf("false");
}

fun isExist(name: String): Boolean {
    return map.containsKey(name);
}

fun isDefined(name: String): Boolean {
    if (!isExist(name)) return false;
    val a = map.get(name);
    val b = a!!.defined;
    return b;
}

fun getType(name: String): String {
    if (!isExist(name)) return "NaN";
    return map.get(name)!!.Type;
}

fun createVar(name: String, type: String): Boolean {
    if (isExist(name) || !isCorrect(name)) return false;
    if (type=="Int") {
        var a = IntClass(name);
        return true;
    }
    if (type=="Bool") {
        var a = BoolClass(name);
        return true;
    }
    return false;
}



fun deleteVar(name: String): Boolean {
    if (!isExist(name)) return false;
    else map.remove(name);
    return true;
}

fun getValue(name: String): String {
    val TYPE = getType(name);
    if (!isExist(name) || TYPE == "NaN") return "NaN";
    if (!isDefined(name)) return "undefined";
    if (TYPE == "Int" || TYPE == "Bool") {
        var a = map.get(name);
        return a!!.value[0];
    }
    return "NaN";
}

fun setValue(name: String, Val: String): Boolean {
    if (!isExist(name)) return false;
    val TYPE = getType(name);
    if (TYPE=="Int") {
        val res = calc(Val);
        if (res=="error") return false;
        val regEx = "[0-9]+".toRegex()  
        map.get(name)!!.value[0] = regEx.find(res)!!.value;
        map.get(name)!!.defined = true;
        return true;
    }
    if (TYPE=="Bool") {
        //доделать
        var res = calc(Val);
        if (res.matches(Regex("([0-9]+|true|false)"))==false) return false;
        if (res=="true" || Val=="false") {
            map.get(name)!!.value[0] = res;
        }
        if (res.matches(Regex("0+"))) {
            map.get(name)!!.value[0] = "false";
        }
        else {
            map.get(name)!!.value[0] = "true";
        }
        map.get(name)!!.defined = true;
        return true;
    }
    return false;
}

abstract class CodeBlock() {
	abstract var Type: String;
	abstract fun execute(): Boolean;
    abstract fun getVars(): MutableSet<String>;
}


class createBlock(x: Array<String>) : CodeBlock() {
 	override var Type = "Create";
 	var instructions = x;
 	override fun execute(): Boolean {
        for (i in 0 until instructions.size step 3) {
            if (instructions[i+2]=="") {
                if (!createVar(instructions[i], instructions[i+1])) return false;
            }
            else {
                if (!createVar(instructions[i], instructions[i+1])) return false;
                if (!setValue(instructions[i], instructions[i+2])) return false;
            }
        }
    	return true;
 	}
    override fun getVars(): MutableSet<String> {
        var res = mutableSetOf<String>();
        for (i in 0 until instructions.size step 3) {
            res.add(instructions[i]);
        }
        return res;
    }
}

class setBlock(x: Array<String>) : CodeBlock() {
 	override var Type = "Set";
 	var instructions = x;
    override fun execute(): Boolean {
        for (i in 0 until instructions.size step 2) {
           if (!setValue(instructions[i], instructions[i+1])) return false;
        }
    	return true;
 	}
    override fun getVars(): MutableSet<String> {
        var res = mutableSetOf<String>();
        for (i in 0 until instructions.size step 2) {
            res.add(instructions[i]);
        }
        return res;
    }
}

class ifBlock(expr: String, ifInstr: Array<CodeBlock>, thenInstr: Array<CodeBlock>) : CodeBlock() {
 	override var Type = "If";
 	var condition = expr;
 	var IF = ifInstr;
 	var THEN = thenInstr;
    override fun execute(): Boolean {
        var DEL: MutableSet<String> = mutableSetOf<String>();
        if (calc(condition)!="0") {
            for(i in IF)	{
        		if (!i.execute()) return false;
                if (i.Type=="Create") {DEL.addAll(i.getVars())}
            }
        }
        else {
            for(i in THEN)	{
        		if (!i.execute()) return false;
                if (i.Type=="Create") {DEL.addAll(i.getVars())}
    		}
        }
        for (j in DEL) {
            
            deleteVar(j)
        }
    	return true;
 	}
    override fun getVars(): MutableSet<String> {
        //доделать (зачем...)
        var res = mutableSetOf<String>();
        return res;
    }
}

class forBlock(before: Array<CodeBlock>, expr: String, after: Array<CodeBlock>, body: Array<CodeBlock>) : CodeBlock() {
 	override var Type = "For";
 	var condition = expr;
 	var start = before;
 	var end = after;
    var cycleBody = body;
    override fun execute(): Boolean {
        var DEL: MutableSet<String> = mutableSetOf();
        for(i in start){
        	if (!i.execute()) {
                return false;
            }
            if (i.Type=="Create") DEL.addAll(i.getVars())
    	}
       
    	while (isTrue(condition)) {
            if (calc(condition)=="error") return false;
            for(i in cycleBody)	{
        		if (!i.execute()) {
           
                	return false;
            	}
                if (i.Type=="Create") DEL.addAll(i.getVars())
            }

            for(i in end){
        		if (!i.execute()) {
                	return false;
            	}
                if (i.Type=="Create") DEL.addAll(i.getVars())
    		}
        }
        for (i in DEL) {
            deleteVar(i);
        }
        return true;
 	}
    override fun getVars(): MutableSet<String> {
        //доделать (зачем...)
        var res = mutableSetOf<String>();
        return res;
    }
}

/*
fun main() {
    var f0 = createBlock(arrayOf("s", "Int", "0.5"))
    var before: Array<CodeBlock>;
    var expr: String;
    var after: Array<CodeBlock>;
    var body: Array<CodeBlock>;
    before = arrayOf(createBlock(arrayOf("i", "Int", "0")))
    expr = "i<8";
    after = arrayOf(setBlock(arrayOf("i", "i+1")))
    body = arrayOf(ifBlock("i%2==0", arrayOf(setBlock(arrayOf("s", "s+i"))), arrayOf()))
    var f1 = forBlock(before, expr, after, body)
    f0.execute()
    f1.execute()
    println(getValue("s"))
    println(isFloat("2.2"))
}
*/
