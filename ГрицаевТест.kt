import java.util.ArrayDeque
//переменные не могут быть пустой строкой, начинаться с цифры и должны содержать символы лат. алфавита, цифры, символы "$", "_"
fun isCorrect(NAME: String): Boolean
{
    if (NAME.length==0) return false;
    if (NAME[0].isDigit()) return false;
    return NAME.matches(Regex("[a-zA-Z0-9_$]+"));
}


//ПАРСЕР НАЧАЛО
fun is_op (c: Char): Boolean {
	return (c=='+' || c=='-' || c=='*' || c=='/' || c=='%');
}


fun priority (ID: Int): Int {
	if (ID < 0)
		return 4; // op == -'+' || op == -'-'
    var op = ID.toChar()
	if (op == '+' || op == '-') return 1;
    if (op == '*' || op == '/' || op == '%') return 2;
	return -1;
}

fun process_op (st: ArrayDeque<Int>, ID: Int): ArrayDeque<Int> {
    var op: Char;
	if (ID < 0) {
		var l = st.last();
        st.removeLast();
        op = (-ID).toChar()
		if (op=='+')  st.addLast(l);
		if (op=='-')  st.addLast(-l);
	}
	else {
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
		}
	}
    return st;
}

fun calc (str: String): Int {
	
    var EXPR = str;
    
    var may_unary = true;
    var s = EXPR.replace("\\s".toRegex(), "")
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
				if (may_unary)  curop = -curop;
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
				while (i < s.length && s[i].isDigit()) {
                    val ch = s[i++]
					OPER+=ch;
                }
				--i;
                st.addLast(OPER.toInt())
				may_unary = false;
			}
            i++
    }
	while (!op.isEmpty()) {
		st = process_op(st, op.last());
        op.removeLast();
    }
    
	return st.last();
}

//ПАРСЕР КОНЕЦ

//КЛАССЫ

//абстрактный класс переменная
abstract class Variable(vName: String) {
	val varName = vName;
	var defined = false;
	abstract var value: String;
	abstract var Type: String;
	
	init {
		map.put(vName, this)
	}
}

//хранилище переменных
var map = mutableMapOf<String, Variable>()

//существует ли переменная?
fun isExist(name: String): Boolean {
    return map.containsKey(name);
}

//определена ли переменная?
fun isDefined(name: String): Boolean {
    if (!isExist(name)) return false;
    val a = map.get(name);
    val b = a!!.defined;
    return b;
}

//класс целое
class IntClass(varName: String) : Variable(varName) {
	override var Type = "Int";
	override var value = "0";
}

//создать переменную, в случае успеха true, иначе false
fun createVar(name: String, type: String): Boolean {
    if (isExist(name) || !isCorrect(name)) return false;
    if (type=="Int") {
        var a = IntClass(name);
        return true;
    }
    return false;
}

//получить значение переменной (для целых)
fun getIntValue(name: String): String {
    if (isExist(name)) {
        var a = map.get(name);
        if (!isDefined(name)) return "undefined";
        else return a!!.value;
    }
    else return "NaN";
}

//присвоить значение переменной (для целых)
fun setIntValue(name: String, Val: Int): Boolean {
    if (isExist(name)) {
        map.get(name)!!.value = Val.toString();
        map.get(name)!!.defined = true;
        return true;
    }
    else return false;
}

