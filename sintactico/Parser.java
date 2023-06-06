package analizador_sintactico1;

import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "");
private final Token select = new Token(TipoToken.SELECT, "select");
private final Token from = new Token(TipoToken.FROM, "from");
private final Token distinct = new Token(TipoToken.DISTINCT, "distinct");
private final Token coma = new Token(TipoToken.COMA, ",");
private final Token punto = new Token(TipoToken.PUNTO, ".");
private final Token asterisco = new Token(TipoToken.ASTERISCO, "*");
private final Token finCadena = new Token(TipoToken.EOF, "");
private final Token classToken = new Token(TipoToken.CLASS, "class");
private final Token fun = new Token(TipoToken.FUN, "fun");
private final Token var = new Token(TipoToken.VAR, "var");
private final Token forToken = new Token(TipoToken.FOR, "for");
private final Token ifToken = new Token(TipoToken.IF, "if");
private final Token print = new Token(TipoToken.PRINT, "print");
private final Token returnToken = new Token(TipoToken.RETURN, "return");
private final Token whileToken = new Token(TipoToken.WHILE, "while");
private final Token trueToken = new Token(TipoToken.TRUE, "true");
private final Token falseToken = new Token(TipoToken.FALSE, "false");
private final Token nullToken = new Token(TipoToken.NULL, "null");
private final Token thisToken = new Token(TipoToken.THIS, "this");
private final Token superToken = new Token(TipoToken.SUPER, "super");
private final Token elseToken = new Token(TipoToken.ELSE, "else");
private final Token number = new Token(TipoToken.NUMBER, "");
private final Token string = new Token(TipoToken.STRING, "");
private final Token equals = new Token(TipoToken.EQUALS, "=");
private final Token semicolon = new Token(TipoToken.SEMICOLON, ";");
private final Token expression = new Token(TipoToken.EXPRESSION, "");
private final Token statement = new Token(TipoToken.STATEMENT, "");
private final Token exprStmt = new Token(TipoToken.EXPR_STMT, "");
private final Token forStmt = new Token(TipoToken.FOR_STMT, "");
private final Token ifStmt = new Token(TipoToken.IF_STMT, "");
private final Token printStmt = new Token(TipoToken.PRINT_STMT, "");
private final Token returnStmt = new Token(TipoToken.RETURN_STMT, "");
private final Token whileStmt = new Token(TipoToken.WHILE_STMT, "");
private final Token block = new Token(TipoToken.BLOCK, "");
private final Token parentesisAbre = new Token(TipoToken.PARENTESIS_ABRE, "(");
private final Token parentesisCierra = new Token(TipoToken.PARENTESIS_CIERRA, ")");
private final Token llaveAbre = new Token(TipoToken.LLAVE_ABRE, "{");
private final Token llaveCierra = new Token(TipoToken.LLAVE_CIERRA, "}");
private final Token asignacion = new Token(TipoToken.ASIGNACION, "=");
private final Token igualdad = new Token(TipoToken.IGUALDAD, "==");
private final Token desigualdad = new Token(TipoToken.DESIGUALDAD, "!=");
private final Token menor = new Token(TipoToken.MENOR, "<");
private final Token menorOigual = new Token(TipoToken.MENOR_O_IGUAL, "<=");
private final Token mayor = new Token(TipoToken.MAYOR, ">");
private final Token mayorOigual = new Token(TipoToken.MAYOR_O_IGUAL, ">=");
private final Token suma = new Token(TipoToken.SUMA, "+");
private final Token resta = new Token(TipoToken.RESTA, "-");
private final Token division = new Token(TipoToken.DIVISION, "/");
private final Token and = new Token(TipoToken.AND, "&&");
private final Token or = new Token(TipoToken.OR, "||");
private final Token negacion = new Token(TipoToken.NEGACION, "!");
private final Token corcheteCierra = new Token(TipoToken.CORCHETE_CIERRA, "]");
private final Token corcheteAbre = new Token(TipoToken.CORCHETE_ABRE, "[");
private final Token dosPuntos = new Token(TipoToken.DOS_PUNTOS, ":");
private final Token numeroEntero = new Token(TipoToken.NUMERO_ENTERO, "");
private final Token numeroDecimal = new Token(TipoToken.NUMERO_DECIMAL, "");
private final Token cadena = new Token(TipoToken.CADENA, "");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        program();

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta válida");
        }

        /*if(!preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }else if(!hayErrores){
            System.out.println("Consulta válida");
        }*/
    }

    private void program() {
        declaration();
    }

    private void declaration() {
        if (match(TipoToken.CLASS)) {
            classDecl();
        } else if (match(TipoToken.FUN)) {
            funDecl();
        } else if (match(TipoToken.VAR)) {
            varDecl();
        } else if (match(TipoToken.STATEMENT)) {
            statement();
        }
    }

    private void classDecl() {
        match(TipoToken.CLASS);
        match(TipoToken.IDENTIFICADOR);
        classInher();
        match(TipoToken.LLAVE_ABRE);
        functions();
        match(TipoToken.LLAVE_CIERRA);
    }

    private void classInher() {
        if (match(TipoToken.MENOR)) {
            match(TipoToken.IDENTIFICADOR);
        }
    }

    private void funDecl() {
        match(TipoToken.FUN);
        function();
    }

    private void varDecl() {
        match(TipoToken.VAR);
        match(TipoToken.IDENTIFICADOR);
        varInit();
        match(TipoToken.PUNTO_COMA);
    }

    private void statement() {
        if (match(TipoToken.EXPR_STMT)) {
            expression();
            match(TipoToken.PUNTO_COMA);
        } else if (match(TipoToken.FOR_STMT)) {
            match(TipoToken.PARENTESIS_ABRE);
            forStmt1();
            forStmt2();
            forStmt3();
            match(TipoToken.PARENTESIS_CIERRA);
            statement();
        } else if (match(TipoToken.IF_STMT)) {
            match(TipoToken.PARENTESIS_ABRE);
            expression();
            match(TipoToken.PARENTESIS_CIERRA);
            statement();
            elseStatement();
        } else if (match(TipoToken.PRINT_STMT)) {
            expression();
            match(TipoToken.PUNTO_COMA);
        } else if (match(TipoToken.RETURN_STMT)) {
            returnExpOpc();
            match(TipoToken.PUNTO_COMA);
        } else if (match(TipoToken.WHILE_STMT)) {
            match(TipoToken.PARENTESIS_ABRE);
            expression();
            match(TipoToken.PARENTESIS_CIERRA);
            statement();
        } else if (match(TipoToken.BLOCK)) {
            match(TipoToken.LLAVE_ABRE);
            blockDecl();
            match(TipoToken.LLAVE_CIERRA);
        }
    }

    private void forStmt1() {
        if (match(TipoToken.VAR)) {
            match(TipoToken.IDENTIFICADOR);
            varInit();
        } else if (match(TipoToken.EXPR_STMT)) {
            expression();
            match(TipoToken.PUNTO_COMA);
        }
    }

    private void forStmt2() {
        if (match(TipoToken.EXPRESSION)) {
            match(TipoToken.PUNTO_COMA);
        }
    }

    private void forStmt3() {
        if (match(TipoToken.EXPRESSION)) {
            expression();
        }
    }

    private void elseStatement() {
        if (match(TipoToken.ELSE)) {
            statement();
        }
    }

    private void varInit() {
        if (match(TipoToken.ASIGNACION)) {
            expression();
        }
    }

    private void expression() {
        assignment();
    }

    private void assignment() {
        logicOr();
        if (match(TipoToken.ASIGNACION)) {
            expression();
        }
    }

    private void logicOr() {
        logicAnd();
        while (match(TipoToken.OR)) {
            logicAnd();
        }
    }

    private void logicAnd() {
        equality();
        while (match(TipoToken.AND)) {
            equality();
        }
    }

    private void equality() {
        comparison();
        while (match(TipoToken.IGUALDAD) || match(TipoToken.DESIGUALDAD)) {
            comparison();
        }
    }

    private void comparison() {
        term();
        while (match(TipoToken.MENOR) || match(TipoToken.MENOR_O_IGUAL) ||
                match(TipoToken.MAYOR) || match(TipoToken.MAYOR_O_IGUAL)) {
            term();
        }
    }

    private void term() {
        factor();
        while (match(TipoToken.SUMA) || match(TipoToken.RESTA)) {
            factor();
        }
    }

    private void factor() {
        unary();
        while (match(TipoToken.DIVISION) || match(TipoToken.ASTERISCO)) {
            unary();
        }
    }

    private void unary() {
        if (match(TipoToken.NEGACION) || match(TipoToken.RESTA)) {
            unary();
        } else {
            call();
        }
    }

    private void call() {
        primary();
        while (match(TipoToken.PUNTO) || match(TipoToken.IDENTIFICADOR)) {
            call2();
        }
    }

    private void call2() {
        if (match(TipoToken.PARENTESIS_ABRE)) {
            argumentsOpc();
            match(TipoToken.PARENTESIS_CIERRA);
            call2();
        } else if (match(TipoToken.PUNTO) && match(TipoToken.IDENTIFICADOR)) {
            call2();
        }
    }

    private void function() {
    match(TipoToken.IDENTIFICADOR);
    match(TipoToken.PARENTESIS_ABRE);
    parameters();
    match(TipoToken.PARENTESIS_CIERRA);
    match(TipoToken.LLAVE_ABRE);
    blockDecl();
    match(TipoToken.LLAVE_CIERRA);
}

private void functions() {
    while (match(TipoToken.FUN)) {
        function();
    }
}
    private void primary() {
        if (match(TipoToken.TRUE) || match(TipoToken.FALSE) || match(TipoToken.NULL) ||
                match(TipoToken.THIS) || match(TipoToken.NUMERO_ENTERO) ||
                match(TipoToken.NUMERO_DECIMAL) || match(TipoToken.CADENA) ||
                match(TipoToken.IDENTIFICADOR) || match(TipoToken.PARENTESIS_ABRE)) {
            // Nada más que hacer
        } else if (match(TipoToken.LLAVE_ABRE)) {
            mapDecl();
        } else if (match(TipoToken.CORCHETE_ABRE)) {
            arrayDecl();
        } else {
            hayErrores = true;
            
        }
    }
private void blockDecl() {
    while (!preanalisis.equals(TipoToken.LLAVE_CIERRA) && !preanalisis.equals(TipoToken.EOF)) {
        statement();
    }
}
    private void mapDecl() {
        match(TipoToken.LLAVE_ABRE);
        keyValuePairList();
        match(TipoToken.LLAVE_CIERRA);
    }

    private void keyValuePairList() {
        if (match(TipoToken.IDENTIFICADOR)) {
            match(TipoToken.DOS_PUNTOS);
            primary();
            keyValuePairList2();
        }
    }

    private void keyValuePairList2() {
        if (match(TipoToken.COMA)) {
            match(TipoToken.IDENTIFICADOR);
            match(TipoToken.DOS_PUNTOS);
            primary();
            keyValuePairList2();
        }
    }

    private void arrayDecl() {
        match(TipoToken.CORCHETE_ABRE);
        primary();
        arrayDecl2();
    }

    private void arrayDecl2() {
        if (match(TipoToken.COMA)) {
            primary();
            arrayDecl2();
        }
        match(TipoToken.CORCHETE_CIERRA);
    }
    private void parameters() {
    match(TipoToken.IDENTIFICADOR);
    parameters2();
}

private void parameters2() {
    if (match(TipoToken.COMA)) {
        match(TipoToken.IDENTIFICADOR);
        parameters2();
    }
}

private void parametersOpc() {
    if (!preanalisis.equals(TipoToken.PARENTESIS_CIERRA)) {
        parameters();
    }
}

    private void argumentsOpc() {
        if (match(TipoToken.EXPRESSION)) {
            arguments();
        }
    }

    private void arguments() {
        expression();
        arguments2();
    }

    private void arguments2() {
        if (match(TipoToken.COMA)) {
            expression();
            arguments2();
        }
    }

    private void returnExpOpc() {
        if (match(TipoToken.EXPRESSION)) {
            expression();
        }
    }

    private boolean match(TipoToken tipoToken) {
        if (preanalisis.tipo.equals(tipoToken)) {
            preanalisis = tokens.get(++i);
            return true;
        } else {
            return false;
        }
    }
}
