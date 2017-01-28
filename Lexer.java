/** Parser for Assignment 2 */

import java.io.*;
import java.util.*;

/** AST ::= BoolConstant | IntConstant | NullConstant | Variable | PrimFun | UnOpApp | BinOpApp | App | Map | If | Let */

/** AST class definitions */

/** The AST type which support a visitor interface */
interface AST {
    public <ResType> ResType accept(ASTVisitor<ResType> v);
}

interface ASTVisitor<ResType> {
    ResType forBoolConstant(BoolConstant b);
    ResType forIntConstant(IntConstant i);
    ResType forNullConstant(NullConstant n);
    ResType forVariable(Variable v);
    ResType forPrimFun(PrimFun f);
    ResType forUnOpApp(UnOpApp u);
    ResType forBinOpApp(BinOpApp b);
    ResType forApp(App a);
    ResType forMap(Map m);
    ResType forIf(If i);
    ResType forLet(Let l);
}

/** Term ::= Constant | PrimFun | Variable */
interface Term extends AST {}

/** Constant ::= IntConstant | BoolConstant | NullConstant */
interface Constant extends Term {}

/** UnOp ::= UnOpPlus | UnOpMinus | OpTilde */
/** Class representing the unary operator within a UnOppApp. */
abstract class UnOp {
    String name;
    public UnOp(String s) { name = s; }
    public String toString() { return name; }
    public abstract <ResType> ResType accept(UnOpVisitor<ResType> v);
}

/** Visitor for the UnOp union type. */
interface UnOpVisitor<ResType> {
    ResType forUnOpPlus(UnOpPlus op);
    ResType forUnOpMinus(UnOpMinus op);
    ResType forOpTilde(OpTilde op);
    // ResType forOpBang(OpBang op);  // Supports ref cell extension to Jam
    // ResType forOpRef(OpRef op);    // Supports ref cell extension to Jam
}

/** BinOp ::= BinOpPlus | BinOpMinus | OpTimes | OpDivide | OpEquals | OpNotEquals | OpLessThan | OpGreaterThan |
 *           OpLessThanOrEquals OpGreaterThanOrEquals | OpAnd | OpOr */

/** Class representing the binary operator within a BinOppApp. */
abstract class BinOp {
    String name;
    public BinOp(String s) { name = s; }
    public String toString() { return name; }
    public abstract <ResType> ResType accept(BinOpVisitor<ResType> v);
}

/** Visitor for the BinOp union type. */
interface BinOpVisitor<ResType> {
    ResType forBinOpPlus(BinOpPlus op);
    ResType forBinOpMinus(BinOpMinus op);
    ResType forOpTimes(OpTimes op);
    ResType forOpDivide(OpDivide op);
    ResType forOpEquals(OpEquals op);
    ResType forOpNotEquals(OpNotEquals op);
    ResType forOpLessThan(OpLessThan op);
    ResType forOpGreaterThan(OpGreaterThan op);
    ResType forOpLessThanEquals(OpLessThanEquals op);
    ResType forOpGreaterThanEquals(OpGreaterThanEquals op);
    ResType forOpAnd(OpAnd op);
    ResType forOpOr(OpOr op);
    // ResType forOpGets(OpGets op);  // Supports the ref cell extension to Jam
}

class UnOpPlus extends UnOp {
    public static final UnOpPlus ONLY = new UnOpPlus();
    private UnOpPlus() { super("+"); }
    public <ResType> ResType accept(UnOpVisitor<ResType> v) {
        return v.forUnOpPlus(this);
    }
}

class UnOpMinus extends UnOp {
    public static final UnOpMinus ONLY = new UnOpMinus();
    private UnOpMinus() { super("-"); }
    public <ResType> ResType accept(UnOpVisitor<ResType> v) {
        return v.forUnOpMinus(this);
    }
}

class OpTilde extends UnOp {
    public static final OpTilde ONLY = new OpTilde();
    private OpTilde() { super("~"); }
    public <ResType> ResType accept(UnOpVisitor<ResType> v) {
        return v.forOpTilde(this);
    }
}

/*  Supports ref cell extension to Jam
  class OpBang extends UnOp {
  public static final OpBang ONLY = new OpBang();
  private OpBang() { super("!"); }
  public <ResType> ResType accept(UnOpVisitor<ResType> v) {
    return v.forOpBang(this); 
  }
}

class OpRef extends UnOp {
  public static final OpRef ONLY = new OpRef();
  private OpRef() { super("ref"); }
  public <ResType> ResType accept(UnOpVisitor<ResType> v) {
    return v.forOpRef(this); 
  }
}
*/

class BinOpPlus extends BinOp {
    public static final BinOpPlus ONLY = new BinOpPlus();
    private BinOpPlus() { super("+"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forBinOpPlus(this);
    }
}

class BinOpMinus extends BinOp {
    public static final BinOpMinus ONLY = new BinOpMinus();
    private BinOpMinus() { super("-"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forBinOpMinus(this);
    }
}

class OpTimes extends BinOp {
    public static final OpTimes ONLY = new OpTimes();
    private OpTimes() { super("*"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpTimes(this);
    }
}

class OpDivide extends BinOp {
    public static final OpDivide ONLY = new OpDivide();
    private OpDivide() { super("/"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpDivide(this);
    }
}

class OpEquals extends BinOp {
    public static final OpEquals ONLY = new OpEquals();
    private OpEquals() { super("="); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpEquals(this);
    }
}

class OpNotEquals extends BinOp {
    public static final OpNotEquals ONLY = new OpNotEquals();
    private OpNotEquals() { super("!="); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpNotEquals(this);
    }
}

class OpLessThan extends BinOp {
    public static final OpLessThan ONLY = new OpLessThan();
    private OpLessThan() { super("<"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpLessThan(this);
    }
}

class OpGreaterThan extends BinOp {
    public static final OpGreaterThan ONLY = new OpGreaterThan();
    private OpGreaterThan() { super(">"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpGreaterThan(this);
    }
}

class OpLessThanEquals extends BinOp {
    public static final OpLessThanEquals ONLY = new OpLessThanEquals();
    private OpLessThanEquals() { super("<="); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpLessThanEquals(this);
    }
}

class OpGreaterThanEquals extends BinOp {
    public static final OpGreaterThanEquals ONLY = new OpGreaterThanEquals();
    private OpGreaterThanEquals() { super(">="); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpGreaterThanEquals(this);
    }
}

class OpAnd extends BinOp {
    public static final OpAnd ONLY = new OpAnd();
    private OpAnd() { super("&"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpAnd(this);
    }
}

class OpOr extends BinOp {
    public static final OpOr ONLY = new OpOr();
    private OpOr() { super("|"); }
    public <ResType> ResType accept(BinOpVisitor<ResType> v) {
        return v.forOpOr(this);
    }
}

/* Supports the ref cell extension to Jam
class OpGets extends BinOp {
  public static final OpGets ONLY = new OpGets();
  private OpGets() { super("<-"); }
  public <ResType> ResType accept(BinOpVisitor<ResType> v) {
    return v.forOpGets(this); 
  }
}
*/

class UnOpApp implements AST {
    private UnOp rator;
    private AST arg;

    UnOpApp(UnOp r, AST a) { rator = r; arg = a; }

    public UnOp rator() { return rator; }
    public AST arg() { return arg; }
    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forUnOpApp(this); }
    public String toString() { return rator + " " + arg; }
}

class BinOpApp implements AST {
    private BinOp rator;
    private AST arg1, arg2;

    BinOpApp(BinOp r, AST a1, AST a2) { rator = r; arg1 = a1; arg2 = a2; }

    public BinOp rator() { return rator; }
    public AST arg1() { return arg1; }
    public AST arg2() { return arg2; }
    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forBinOpApp(this); }
    public String toString() {
        return "(" + arg1 + " " + rator + " " + arg2 + ")";
    }
}

class Map implements AST {
    private Variable[] vars;
    private AST body;

    Map(Variable[] v, AST b) { vars = v; body = b; }
    public Variable[] vars() { return vars; }
    public AST body() { return body; }
    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forMap(this); }
    public String toString() {
        return "map " + ToString.toString(vars,",") + " to " + body ;
    }
}

class App implements AST {
    private AST rator;
    private AST[] args;

    App(AST r, AST[] a) { rator = r; args = a; }

    public AST rator() { return rator; }
    public AST[] args() { return args; }

    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forApp(this); }
    public String toString() {
        if ((rator instanceof PrimFun) || (rator instanceof Variable))
            return rator + "(" + ToString.toString(args,", ") + ")";
        else
            return "(" +  rator + ")(" + ToString.toString(args,", ") + ")";
    }
}

class If implements AST {
    private AST test, conseq, alt;
    If(AST t, AST c, AST a) { test = t; conseq = c; alt = a; }

    public AST test() { return test; }
    public AST conseq() { return conseq; }
    public AST alt() { return alt; }
    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forIf(this); }
    public String toString() {
        return "if " + test + " then " + conseq + " else " + alt ;
    }
}

class Let implements AST {
    private Def[] defs;
    private AST body;
    Let(Def[] d, AST b) { defs = d; body = b; }

    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forLet(this); }
    public Def[] defs() { return defs; }
    public AST body() { return body; }
    public String toString() {
        return "let " + ToString.toString(defs," ") + " in " + body;
    }
}

/** Def class representing a definition embedded inside a Let. */
class Def {
    private Variable lhs;
    private AST rhs;

    Def(Variable l, AST r) { lhs = l; rhs = r; }
    public Variable lhs() { return lhs; }
    public AST rhs() { return rhs; }

    public String toString() { return lhs + " := " + rhs + ";"; }
}

/** Dummy class containing an improved toString method for arrays. */
class ToString {

    public static String toString(Object[] a, String s) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            if (i > 0) result.append(s);
            Object elt = a[i];
            String eltString = (elt instanceof Object[]) ?
                    toString((Object[]) elt, s) : elt.toString();
            result.append(eltString);
        }
        return result.toString();
    }
}

/** Exception class representing a parser error. */
class ParseException extends RuntimeException {
    ParseException(String s) { super(s); }
}

/** JamVal and Token Data Definitions */

/** A data object representing a Jam value */
interface JamVal {
    <ResType> ResType accept(JamValVisitor<ResType> jvv);
}

/** A visitor for the JamVal type (Jam values).\ */
interface JamValVisitor<ResType> {
    ResType forIntConstant(IntConstant ji);
    ResType forBoolConstant(BoolConstant jb);
    ResType forJamList(JamList jl);
    ResType forJamFun(JamFun jf);
    // ResType forJamVoid(JamVoid jf);  // Supports the addition of recursive let to Jam
}

/** A data object representing a Jam token */
interface Token {}

/** JamVal classes */

/** a Jam integer constant, also used to represent an integer token for parsing  */
class IntConstant implements Token, Constant, JamVal {
    private int value;

    IntConstant(int i) { value = i; }
    // duplicates can occur!

    public int value() { return value; }

    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forIntConstant(this); }
    public <ResType> ResType accept(JamValVisitor<ResType> v) { return v.forIntConstant(this); }
    /** redefines equals so that equal integers are recognized as equal */
    public boolean equals(Object other) {
        return (other != null && this.getClass() == other.getClass()) &&
                (value == ((IntConstant)other).value());
    }
    /** computes the obvious hashcode for this consistent with equals */
    public int hashcode() { return value; }
    public String toString() { return String.valueOf(value); }
}

/** A Jam boolean constant, also used to represent a boolean token for parsing */
class BoolConstant implements Token, Constant, JamVal {
    private boolean value;
    private BoolConstant(boolean b) { value = b; }

    /** singleton pattern definitions */
    public static final BoolConstant FALSE = new BoolConstant(false);
    public static final BoolConstant TRUE = new BoolConstant(true);

    /** factory method that returns BoolConstant corresponding to b */
    public static BoolConstant toBoolConstant(boolean b) {
        if (b) return TRUE;
        else return FALSE;
    }

    public boolean value() { return value; }
    public BoolConstant not() { if (this == FALSE) return TRUE; else return FALSE; }

    public <ResType> ResType accept(ASTVisitor<ResType> av) { return av.forBoolConstant(this); }
    public <ResType> ResType accept(JamValVisitor<ResType> jv) { return jv.forBoolConstant(this); }
    public String toString() { return String.valueOf(value); }
}

/** Immutable List and Binding Classes */

interface PureList<ElemType> {
    abstract PureList<ElemType> cons(ElemType o);
    abstract PureList<ElemType> empty();
    abstract <ResType> ResType accept(PureListVisitor<ElemType, ResType> v);
    abstract String toStringHelp();
    abstract PureList<ElemType> append(PureList<ElemType> addedElts);
}

/** The visitor interface for the type PureList<T> */
interface PureListVisitor<ElemType, ResType> {
    ResType forEmpty(Empty<ElemType> e);
    ResType forCons(Cons<ElemType> c);
}

/** An abstract class that factors out code common to classes Empty<T> and Cons<T> */
abstract class PureListClass<ElemType> implements PureList<ElemType> {
    public PureList<ElemType> cons(ElemType o) { return new Cons<ElemType>(o,this); }
    public PureList<ElemType> empty() { return new Empty<ElemType>(); }
    public abstract <ResType> ResType accept(PureListVisitor<ElemType, ResType> v);
    // preceding DICTATED BY BUG IN JSR-14
}

/** The empty PureList<T> class */
class Empty<ElemType> extends PureListClass<ElemType> {
    public <ResType> ResType accept(PureListVisitor<ElemType,ResType> v) { return v.forEmpty(this); }
    public PureList<ElemType> append(PureList<ElemType> addedElts) { return addedElts; }

    /** overrides inherited equals because Empty is not a singleton! */
    public boolean equals(Object other) {
        return (other != null && other.getClass() == this.getClass());
    }

    public String toString() { return "()"; }
    public String toStringHelp() { return ""; }
}

/** The non-empty PureList<T> class */
class Cons<ElemType> extends PureListClass<ElemType> {
    ElemType first;
    PureList<ElemType> rest;
    Cons(ElemType f, PureList<ElemType> r) { first = f; rest = r; }

    public <ResType> ResType accept(PureListVisitor<ElemType,ResType> v) { return v.forCons(this); }
    public PureList<ElemType> append(PureList<ElemType> addedElts) {
        return new Cons<ElemType>(first, rest.append(addedElts));
    }

    public ElemType first() { return first; }
    public PureList<ElemType> rest() { return rest; }

    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) return false;
        Cons otherCons = (Cons) other;
        return first.equals(otherCons.first) && rest.equals(otherCons.rest);
    }

    public String toString() { return "(" + first + rest.toStringHelp() + ")"; }

    public String toStringHelp() { return " " + first + rest.toStringHelp(); }
}

/** A Jam list */
interface JamList extends PureList<JamVal>, JamVal {
    JamEmpty empty();
    JamCons cons(JamVal v);
}

class JamEmpty extends Empty<JamVal> implements JamList {
    public static final JamEmpty ONLY = new JamEmpty();
    private JamEmpty() {}

    public JamEmpty empty() { return ONLY; }
    public JamCons cons(JamVal v) { return new JamCons(v, this); }
    public <ResType> ResType accept(JamValVisitor<ResType> v) { return v.forJamList(this); }
}

class JamCons extends Cons<JamVal> implements JamList {
    public JamCons(JamVal v, JamList vList) {
        super(v, vList);
    }
    public JamEmpty empty() { return JamEmpty.ONLY; }
    public JamCons cons(JamVal v) { return new JamCons(v, this); }

    public <ResType> ResType accept(JamValVisitor<ResType> v) { return v.forJamList(this); }
    public JamList rest() { return (JamList) super.rest(); }
}

/** The basic Jam binding framework. */
abstract class Binding {
    Variable var;
    JamVal value;
    Binding(Variable v, JamVal jv) {
        var = v; value = jv;
    }
    public Variable var() { return var; }
    public JamVal value() { return value; }
    void putValue(JamVal v) { value = v; }
}

/** Other JamVal classes */

/** a Jam function (closure or primitive function) */
abstract class JamFun implements JamVal {
    public <ResType> ResType accept(JamValVisitor<ResType> jvv) { return jvv.forJamFun(this); }
    abstract public <ResType> ResType accept(JamFunVisitor<ResType> jfv);
}

/** The visitor interface for the JamFun type */
interface JamFunVisitor<ResType> {
    ResType forJamClosure(JamClosure c);
    ResType forPrimFun(PrimFun pf);
}

/** a Jam closure */
class JamClosure extends JamFun {
    private Map body;
    private PureList<Binding> env;

    JamClosure(Map b, PureList<Binding> e) { body = b; env = e; }
    Map body() { return body; }
    PureList<Binding> env() { return env; }
    public <ResType> ResType accept(JamFunVisitor<ResType> jfv) { return jfv.forJamClosure(this); }
}

/** a Jam Primitive Function */
abstract class PrimFun extends JamFun implements Token, Term {
    private String name;
    PrimFun(String n) { name = n; }
    public String name() { return name; }
    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forPrimFun(this); }
    public <ResType> ResType accept(JamFunVisitor<ResType> v) { return v.forPrimFun(this); }
    abstract public <ResType> ResType accept(PrimFunVisitor<ResType> pfv);
    public String toString() { return name; }
}

/** a dummy Jam value used to implement recursive let */
/* 
 class JamVoid implements JamVal {
  public static final JamVoid ONLY = new JamVoid();
  private JamVoid() {}
  public <ResType> ResType accept(JamValVisitor<ResType> jvv) { return jvv.forJamVoid(this); }
}
*/

/** a visitor for PrimFun classes */
interface PrimFunVisitor<ResType> {
    ResType forFunctionPPrim();
    ResType forNumberPPrim();
    ResType forListPPrim();
    ResType forConsPPrim();
    ResType forNullPPrim();
    ResType forArityPrim();
    ResType forConsPrim();
    ResType forFirstPrim();
    ResType forRestPrim();
}

class FunctionPPrim extends PrimFun {
    public static final FunctionPPrim ONLY = new FunctionPPrim();
    private FunctionPPrim() { super("function?"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forFunctionPPrim(); }
}

class NumberPPrim extends PrimFun {
    public static final NumberPPrim ONLY = new NumberPPrim();
    private NumberPPrim() { super("number?"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forNumberPPrim(); }
}
class ListPPrim extends PrimFun {
    public static final ListPPrim ONLY = new ListPPrim();
    private ListPPrim() { super("list?"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forListPPrim(); }
}
class ConsPPrim extends PrimFun {
    public static final ConsPPrim ONLY = new ConsPPrim();
    private ConsPPrim() { super("cons?"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forConsPPrim(); }
}
class NullPPrim extends PrimFun {
    public static final NullPPrim ONLY = new NullPPrim();
    private NullPPrim() { super("null?"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forNullPPrim(); }
}
class RefPPrim extends PrimFun {
    public static final RefPPrim ONLY = new RefPPrim();
    private RefPPrim() { super("ref?"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forNullPPrim(); }
}
class ArityPrim extends PrimFun {
    public static final ArityPrim ONLY = new ArityPrim();
    private ArityPrim() { super("arity"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forArityPrim(); }
}
class ConsPrim extends PrimFun {
    public static final ConsPrim ONLY = new ConsPrim();
    private ConsPrim() { super("cons"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forConsPrim(); }
}
class FirstPrim extends PrimFun {
    public static final FirstPrim ONLY = new FirstPrim();
    private FirstPrim() { super("first"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forFirstPrim(); }
}
class RestPrim extends PrimFun {
    public static final RestPrim ONLY = new RestPrim();
    private RestPrim() { super("rest"); }
    public <ResType> ResType accept(PrimFunVisitor<ResType> pfv) { return pfv.forRestPrim(); }
}


/** Token classes */

class NullConstant implements Token, Constant {
    public static final NullConstant ONLY = new NullConstant();
    private NullConstant() {}
    public <T> T accept(ASTVisitor<T> v) { return v.forNullConstant(this); }
    public String toString() { return "null"; }
}

class Variable implements Token, Term {
    private String name;
    Variable(String n) { name = n; }

    public String name() { return name; }
    public <ResType> ResType accept(ASTVisitor<ResType> v) { return v.forVariable(this); }
    public String toString() { return name; }
}

class OpToken implements Token {
    private String symbol;
    private boolean isUnOp;
    private boolean isBinOp;
    /** the corresponding unary operator in UnOp */
    private UnOp unOp;
    /** the corresponding binary operator in BinOp */
    private BinOp binOp;

    private OpToken(String s, boolean iu, boolean ib, UnOp u, BinOp b) {
        symbol = s; isUnOp = iu; isBinOp = ib; unOp = u; binOp = b;
    }

    /** factory method for constructing OpToken serving as both UnOp and BinOp */
    public static OpToken newBothOpToken(String s, UnOp u, BinOp b) {
        return new OpToken(s, true, true, u, b);
    }

    /** factory method for constructing OpToken serving as BinOp only */
    public static OpToken newBinOpToken(String s, BinOp b) {
        return new OpToken(s, false, true, null, b);
    }

    /** factory method for constructing OpToken serving as UnOp only */
    public static OpToken newUnOpToken(String s, UnOp u) {
        return new OpToken(s, true, false, u, null);
    }
    public String symbol() { return symbol; }
    public boolean isUnOp() { return isUnOp; }
    public boolean isBinOp() { return isBinOp; }
    public UnOp toUnOp() {
        if (unOp == null)
            throw new NoSuchElementException("OpToken " + this + " does not denote a unary operator");
        return unOp;
    }

    public BinOp toBinOp() {
        if (binOp == null)
            throw new NoSuchElementException("OpToken " + this + " does not denote a binary operator");
        return binOp;
    }
    public String toString() { return symbol; }
}

class KeyWord implements Token {
    private String name;

    KeyWord(String n) { name = n; }
    public String name() { return name; }
    public String toString() { return name; }
}

class LeftParen implements Token {
    public String toString() { return "("; }
    private LeftParen() {}
    public static final LeftParen ONLY = new LeftParen();
}

class RightParen implements Token {
    public String toString() { return ")"; }
    private RightParen() {}
    public static final RightParen ONLY = new RightParen();
}

class LeftBrack implements Token {
    public String toString() { return "["; }
    private LeftBrack() {}
    public static final LeftBrack ONLY = new LeftBrack();
}

class RightBrack implements Token {
    public String toString() { return "]"; }
    private RightBrack() {}
    public static final RightBrack ONLY = new RightBrack();
}

/* Supports the addition of blocks to Jam
  class LeftBrace implements Token {
  public String toString() { return "{"; }
  private LeftBrace() {}
  public static final LeftBrace ONLY = new LeftBrace();
}

class RightBrace implements Token {
  public String toString() { return "}"; }
  private RightBrace() {}
  public static final RightBrace ONLY = new RightBrace();
}
*/

class Comma implements Token {
    public String toString() { return ","; }
    private Comma() {}
    public static final Comma ONLY = new Comma();
}

class SemiColon implements Token {
    public String toString() { return ";"; }
    private SemiColon() {}
    public static final SemiColon ONLY = new SemiColon();
}

class EndOfFile implements Token {
    public String toString() { return "*EOF*"; }
    private EndOfFile() {}
    public static final EndOfFile ONLY = new EndOfFile();
}

/** Jam lexer class.              
 *  Given a Lexer object, the next token in that input stream being
 *  processed by the Lexer is returned by static method readToken(); it
 *  throws a ParseException (an extension of IOException) if it
 *  encounters a syntax error.  Calling readToken() advances the cursor
 *  in the input stream to the next token.
 *
 *  The static method peek() in the Lexer class has the same behavior as
 *  readToken() except for the fact that it does not advance the cursor.
 */
class Lexer extends StreamTokenizer {

    // short names for StreamTokenizer codes

    public static final int WORD = StreamTokenizer.TT_WORD;
    public static final int NUMBER = StreamTokenizer.TT_NUMBER;
    public static final int EOF = StreamTokenizer.TT_EOF;
    public static final int EOL = StreamTokenizer.TT_EOL;

    /** The wordtable for classifying words (identifiers/operators) in token stream */
    public HashMap<String,Token>  wordTable = new HashMap<String,Token>();

    // Lexer peek cannot be implemented using StreamTokenizer pushBack
    // because some Tokens are composed of two StreamTokenizer tokens

    Token buffer;  // holds token for peek() operation

    Lexer(Reader inputStream) {
        super(new BufferedReader(inputStream));
        initLexer();

    }

    Lexer(String fileName) throws IOException {
        this(new FileReader(fileName));
    }

    private void initLexer() {

        // configure StreamTokenizer portion of this
        resetSyntax();
        parseNumbers();
        ordinaryChar('-');
        slashSlashComments(true);
        wordChars('0','9');
        wordChars('a','z');
        wordChars('A','Z');
        wordChars('_','_');
        wordChars('?','?');
        whitespaceChars(0,' ');

        // `+' `-' `*' `/' `~' `=' `!' `<' `>' `&' `|' `:' `;' `,'
        // `(' `)' `[' `]'  are ordinary characters (self-delimiting)

        initWordTable();
        buffer = null;  // buffer initially empty
    }

    public void flush() throws IOException {
        eolIsSignificant(true);
        while (nextToken() != EOL) ; // eat tokens until EOL
        eolIsSignificant(false);
    }

    public Token peek() {
        if (buffer == null) buffer = readToken();
        return buffer;
    }

    private int getToken() {
        // synonymous with nextToken() except for throwing an unchecked
        // ParseException instead of a checked IOException
        try {
            int tokenType = nextToken();
            return tokenType;
        } catch(IOException e) {
            throw new ParseException("IOException " + e + "thrown by nextToken()");
        }
    }

    public Token readToken() {

        // uses getToken() to read next token
        // constructs Token object representing that token
        // NOTE: token representations for all Token classes except
        //   IntConstant are unique; a HashMap is used to avoid duplication
        //   Hence, == can safely be used to compare all Tokens except IntConstants
        //   for equality

        if (buffer != null) {
            Token token = buffer;
            buffer = null;          // clear buffer
            return token;
        }

        int tokenType = getToken();
        switch (tokenType) {
            case NUMBER:
                int value = (int) nval;
                if (nval == (double) value) return new IntConstant(value);
                throw
                        new ParseException("The number " + nval + " is not a 32 bit integer");
            case WORD:
                Token regToken = wordTable.get(sval);
                if (regToken == null) {
                    // must be new variable name
                    Variable newVar = new Variable(sval);
                    wordTable.put(sval,newVar);
                    return newVar;
                }
                return regToken;
            case EOF: return EndOfFile.ONLY;
            case '(': return LeftParen.ONLY;
            case ')': return RightParen.ONLY;
            case '[': return LeftBrack.ONLY;
            case ']': return RightBrack.ONLY;
            // case '{': return LeftBrace.ONLY;   // Supports the addition of blocks to Jam
            // case '}': return RightBrace.ONLY;  // Supports the addition of blocks to Jam
            case ',': return Comma.ONLY;
            case ';': return SemiColon.ONLY;

            case '+': return wordTable.get("+");
            case '-': return wordTable.get("-");
            case '*': return wordTable.get("*");
            case '/': return wordTable.get("/");
            case '~': return wordTable.get("~");
            case '=': return wordTable.get("=");
            case '<':
                tokenType = getToken();
                if (tokenType == '=') return wordTable.get("<=");
                // if (tokenType == '-') return wordTable.get("<-");  // Support the addition of ref cells to Jam
                pushBack();
                return wordTable.get("<");
            case '>':
                tokenType = getToken();
                if (tokenType == '=') return wordTable.get(">=");
                pushBack();
                return wordTable.get(">");
            case '!':
                tokenType = getToken();
                if (tokenType == '=') return wordTable.get("!=");
                throw new
                        ParseException("`" + ((char) tokenType) + "' is not a legal token");
                // Support the addition of ref cells to Java (remove preceding statement)
                // pushBack();
                // return wordTable.get("!");
            case '&': return wordTable.get("&");
            case '|': return wordTable.get("|");
            case ':': {
                tokenType = getToken();
                if (tokenType == '=') return wordTable.get(":=");
                pushBack();
                throw new ParseException("`:' is not a legal token");
            }
            default:
                throw new
                        ParseException("`" + ((char) tokenType) + "' is not a legal token");
        }
    }

    private void initWordTable() {
        // initialize wordTable

        // constants
        // <null>  ::= null
        // <bool>  ::= true | false

        wordTable.put("null",  NullConstant.ONLY);
        wordTable.put("true",  BoolConstant.TRUE);
        wordTable.put("false", BoolConstant.FALSE);

        //  Install symbols constructed from self-delimiting characters

        // operators
        // <unop>  ::= <sign> | ~   // formerly | ! | ref
        // <binop> ::= <sign> | "*" | / | = | != | < | > | <= | >= | & | "|"
        // <sign>  ::= "+" | -

        //  Note: there is no class distinction between <unop> and <binop> at
        //  lexical level because of ambiguity; <sign> belongs to both

        wordTable.put("+",   OpToken.newBothOpToken("+", UnOpPlus.ONLY, BinOpPlus.ONLY));
        wordTable.put("-",   OpToken.newBothOpToken("-", UnOpMinus.ONLY, BinOpMinus.ONLY));

        wordTable.put("~",   OpToken.newUnOpToken("~",   OpTilde.ONLY));
        // Supports addition of ref cells to Jam
        // wordTable.put("!",   OpToken.newUnOpToken("!",   OpBang.ONLY));
        // wordTable.put("ref", OpToken.newUnOpToken("ref", OpRef.ONLY));

        wordTable.put("*",   OpToken.newBinOpToken("*", OpTimes.ONLY));
        wordTable.put("/",   OpToken.newBinOpToken("/", OpDivide.ONLY));
        wordTable.put("=",   OpToken.newBinOpToken("=", OpEquals.ONLY));
        wordTable.put("!=",  OpToken.newBinOpToken("!=",OpNotEquals.ONLY));
        wordTable.put("<",   OpToken.newBinOpToken("<", OpLessThan.ONLY));
        wordTable.put(">",   OpToken.newBinOpToken(">", OpGreaterThan.ONLY));
        wordTable.put("<=",  OpToken.newBinOpToken("<=",OpLessThanEquals.ONLY));
        wordTable.put(">=",  OpToken.newBinOpToken(">=",OpGreaterThanEquals.ONLY));
        wordTable.put("&",   OpToken.newBinOpToken("&", OpAnd.ONLY));
        wordTable.put("|",   OpToken.newBinOpToken("|", OpOr.ONLY));
        // Supports addition of ref cells to Jam
        // wordTable.put("<-",  OpToken.newBinOpToken("<-",OpGets.ONLY));

        // Install primitive functions
        // <prim>  ::= number? | function? | list? | null? | cons? ref? |
        //             arity | cons | first | rest

        wordTable.put("number?",   NumberPPrim.ONLY);
        wordTable.put("function?", FunctionPPrim.ONLY);
        wordTable.put("list?",     ListPPrim.ONLY);
        wordTable.put("null?",     NullPPrim.ONLY);
        wordTable.put("cons?",     ConsPPrim.ONLY);
        wordTable.put("ref?",      RefPPrim.ONLY);
        wordTable.put("arity",     ArityPrim.ONLY);
        wordTable.put("cons",      ConsPrim.ONLY);
        wordTable.put("first",     FirstPrim.ONLY);
        wordTable.put("rest",      RestPrim.ONLY);


        // keywords: if then else let in map to :=
        wordTable.put("if",   new KeyWord("if"));
        wordTable.put("then", new KeyWord("then"));
        wordTable.put("else", new KeyWord("else"));
        wordTable.put("let",  new KeyWord("let"));
        wordTable.put("in",   new KeyWord("in"));
        wordTable.put("map",  new KeyWord("map"));
        wordTable.put("to",   new KeyWord("to"));
        wordTable.put(":=",   new KeyWord(":="));

    }

    public static void main(String[] args) throws IOException {
        // check for legal argument list
        if (args.length == 0) {
            System.out.println("Usage: java Lexer <filename>");
            return;
        }
        Lexer in = new Lexer(args[0]);
        do {
            Token t = in.readToken();
            if (t == null) break;
            System.out.println("Token " + t + " in " + t.getClass());
        } while (true);
    }
}

class Test {
    static String s1 = "let f :=  map n to if n = 0 then 1 else n * f(n - 1); in f(3)";
    static StringReader in1 = new StringReader(s1);
}


