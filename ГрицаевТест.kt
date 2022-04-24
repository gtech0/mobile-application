import java.util.ArrayDeque
//переменные не могут быть пустой строкой, начинаться с цифры и должны содержать символы лат. алфавита, цифры, символы "$", "_"
fun Boolean.toInt() = if (this) 1 else 0;
fun Int.toBool() = if (this==0) false else true;
fun isCorrect(NAME: String): Boolean
{
    if (NAME.length==0) return false;
    if (NAME[0].isDigit()) return false;
    return NAME.matches(Regex("[a-zA-Z0-9_$]+"));
}


//ПАРСЕР НАЧАЛО
fun is_op (c: Char): Boolean {
    val s = arrayOf('+', '-', '*', '/', '%', '|', '&', '!', '<', '>', '⩽', '⩾', '=', '≠')
	return (c in s);
}


fun priority (ID: Int): Int {
	if (ID < 0) {
    	if (ID.toChar() == '!') return 3;
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

fun process_op (st: ArrayDeque<Int>, ID: Int): ArrayDeque<Int> {
    var op: Char;
	if (ID < 0) {
        if (st.count() == 0) return st;
		var l = st.last();
        st.removeLast();
        op = (-ID).toChar()
		if (op=='+')  st.addLast(l);
		if (op=='-')  st.addLast(-l);
        if (op=='!')  st.addLast((!(l.toBool())).toInt());
	}
	else {
        if (st.count() < 2) {
            st.clear();
            return st;
        }
		var r = st.last();
        st.removeLast();
		var l = st.last();
        st.removeLast();
        op = ID.toChar()
		when(op) {
			'+' ->  st.addLast(l + r);
			'-' ->  st.addLast(l - r);  
			'*' ->  st.addLast(l * r);
			'/' ->  st.addLast(l / r);
			'%' ->  st.addLast(l % r);
			'|' ->  st.addLast((l.toBool() || r.toBool()).toInt());
            '&' ->  st.addLast((l.toBool() && r.toBool()).toInt());
            '<' ->  st.addLast((l < r).toInt());
            '>' ->  st.addLast((l > r).toInt());
            '=' ->  st.addLast((l == r).toInt());
            '⩽' ->  st.addLast((l <= r).toInt());
            '⩾' ->  st.addLast((l >= r).toInt());
            '≠' -> st.addLast((l != r).toInt());
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
    var st = ArrayDeque<Int>()
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
                if (OPER[0].isDigit()) st.addLast(OPER.toInt())
                else { 
                    if (isDefined(OPER) && (getType(OPER)=="Int" || getType(OPER)=="Bool")) {
                        st.addLast(getValue(OPER).toInt())
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

//ПАРСЕР КОНЕЦ

//КЛАССЫ
abstract class Variable(vName: String) {
val varName = vName;
var defined = false;
abstract var value: String;
abstract var Type: String;
    init {
       map.put(vName, this)
    }
}

var map = mutableMapOf<String, Variable>()


class IntClass(varName: String) : Variable(varName) {
override var Type = "Int";
 override var value = "0";
}

class BoolClass(varName: String) : Variable(varName) {
override var Type = "Bool";
 override var value = "false";
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

fun getValue(name: String): String {
    val TYPE = getType(name);
    if (!isExist(name) || TYPE == "NaN") return "NaN";
    if (!isDefined(name)) return "undefined";
    if (TYPE == "Int" || TYPE == "Bool") {
        var a = map.get(name);
        return a!!.value;
    }
    return "NaN";
}

fun setValue(name: String, Val: String): Boolean {
    if (!isExist(name)) return false;
    val TYPE = getType(name);
    if (TYPE=="Int") {
        val res = calc(Val);
        if (res=="error") return false;
        map.get(name)!!.value = res;
        map.get(name)!!.defined = true;
        return true;
    }
    if (TYPE=="Bool") {
        //доделать
        if (Val!="0" && Val!="1") return false;
        if (Val=="0") map.get(name)!!.value = "false";
        else map.get(name)!!.value = "true";
        map.get(name)!!.defined = true;
        return true;
    }
    return false;
}

