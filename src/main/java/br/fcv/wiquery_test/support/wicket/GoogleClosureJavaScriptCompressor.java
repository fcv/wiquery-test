package br.fcv.wiquery_test.support.wicket;

import org.apache.wicket.javascript.IJavaScriptCompressor;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Compiler;

public class GoogleClosureJavaScriptCompressor implements IJavaScriptCompressor {
    
    private static final GoogleClosureJavaScriptCompressor INSTANCE = new GoogleClosureJavaScriptCompressor();
    
    public static GoogleClosureJavaScriptCompressor getInstance() {
        return INSTANCE;
    }

    /**
     * 
     * @see http://code.google.com/p/closure-compiler/wiki/FAQ#How_do_I_call_Closure_Compiler_from_the_Java_API?
     * @see http://blog.bolinfest.com/2009/11/calling-closure-compiler-from-java.html
     */
    @Override
    public String compress(String original) {
        Compiler compiler = new Compiler();

        CompilerOptions options = new CompilerOptions();
        // Advanced mode is used here, but additional options could be set, too.
        CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(
            options);

        // To get the complete set of externs, the logic in
        // CompilerRunner.getDefaultExterns() should be used here.
        JSSourceFile extern = JSSourceFile.fromCode("externs.js",
            "function alert(x) {}");

        // The dummy input name "input.js" is used here so that any warnings or
        // errors will cite line numbers in terms of input.js.
        JSSourceFile input = JSSourceFile.fromCode("input.js", original);

        // compile() returns a Result, but it is not needed here.
        compiler.compile(extern, input, options);

        // The compiler is responsible for generating the compiled code; it is not
        // accessible via the Result.
        return compiler.toSource();
    }

}
