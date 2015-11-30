/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package thirdCore;

import Formais2.Tokens;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author gabluz
 */
public class Sintatico {
    private static HashMap<String, HashMap<String, Producao>> tabela;
    
    public static ArrayList<Token> tokensToToken(Tokens tokensOLD) {
        
        ArrayList<String[]> listTokensOLD = tokensOLD.getAll();
        ArrayList<Token> newTokens = new ArrayList();
        for(String[] a : listTokensOLD) {
            Token newToken = new Token(a[0], a[1]);
            newTokens.add(newToken);
        }
        
        return newTokens;
    }
    
    
    public static boolean analise(ArrayList<Token> tokens, HashMap<String, HashMap<String, Producao>> tab) {
        tabela = tab;
        Stack pilha = new Stack();
        Token teste = new Token("prg", "PR");
        tokens.add(teste);
        teste = new Token(null, "ID");
        tokens.add(teste);
        teste = new Token("var", "PR");
        tokens.add(teste);
        teste = new Token(null, "ID");
        tokens.add(teste);
        teste = new Token(":", "SIMB");
        tokens.add(teste);
        teste = new Token("int", "TIPO");
        tokens.add(teste);
        teste = new Token(";", "SIMB");
        tokens.add(teste);
        teste = new Token("go", "PR");
        tokens.add(teste);
        teste = new Token(null, "ID");
        tokens.add(teste);
        teste = new Token("=", "SIMB");
        tokens.add(teste);
        teste = new Token("9", "NUM");
        tokens.add(teste);
        teste = new Token(";", "SIMB");
        tokens.add(teste);
        teste = new Token("end", "PR");
        tokens.add(teste);
        teste = new Token(";", "SIMB");
        //tokens.add(teste);
        //teste = new Token("prg", "PR");
        
        
        
        
        
        Token t = new Token("$", "PR");
        tokens.add(t); //Adiciona $ no fim da entrada
        pilha.push(t);//Adiciona $ no fundo da pilha
        t = new Token(null, "S");
        pilha.push(t); //Pilha agora é $S
        
        Token topoPilha;
        String X;
        Producao prod;
        ArrayList<Token> producao;
        Token helpwhile;
        int apontador = 0;
        Token entrada;
        
        ArrayList<Producao> saida = new ArrayList();
        
        do {
            System.out.println("LOOP AGAIN");
            topoPilha = (Token) pilha.peek();
            System.out.println("TOPO " + topoPilha.getUsarNaGramatica() + " E " + topoPilha.eFinal());
            entrada = tokens.get(apontador);
            System.out.println("Entrada Atual " + tokens.get(apontador).getUsarNaGramatica());
            if(topoPilha.eFinal()) {
               // System.out.println("ENTROU AQUI");
                X = topoPilha.getUsarNaGramatica();
                
                if(X.compareTo(entrada.getUsarNaGramatica()) == 0) {
                    //System.out.println("ENTROU AQUI2");
                    pilha.pop();
                    apontador++;
                } else {
                    System.out.println("AQUI");
                    return false;
                }
            } else {
                System.out.println("at least");
                prod = tabela.get(topoPilha.getUsarNaGramatica()).get(entrada.getUsarNaGramatica());
                if(prod != null) {
                    pilha.pop();
                    producao = prod.getCorpo();
                    
                    if(prod.getCorpo().get(0).getUsarNaGramatica().compareTo("&") != 0) {
                       System.out.println("NOVA PRODUCAO a seguir");
                        for (int i = producao.size() - 1; i > -1; i--) {
                            pilha.push(producao.get(i));
                            System.out.println(producao.get(i).getUsarNaGramatica());
                            
                        }
                        
                        saida.add(prod);
                    }
                } else {
                    return false;
                }
            }
            helpwhile = (Token) pilha.peek();
            
        } while(helpwhile.getUsarNaGramatica().compareTo("$") != 0);
        
        
        
        //Gerar arvore gramatical
        TreeNode<Token> tree = new TreeNode(new Token(null, "S"));
        System.out.println(saida.size());
        Sintatico.doTree(tree, saida);
        return true;
        
    }
    
    public static void doTree(TreeNode nodoatual, ArrayList<Producao> saida ) {
        if(!saida.isEmpty()) {
            Token t = (Token) nodoatual.data;
            if(!t.eFinal()) {
                for(int i = 0; i < saida.get(0).getCorpo().size(); i++) {
                    nodoatual.addChild(saida.get(0).getCorpo().get(i));
                }
                saida.remove(0);
                for(int i = 0; i < nodoatual.children.size(); i++) {
                    TreeNode filho = (TreeNode) nodoatual.children.get(0);
                    Sintatico.doTree(filho, saida);
                }
                
            }
        }
        
    }
    
}//
