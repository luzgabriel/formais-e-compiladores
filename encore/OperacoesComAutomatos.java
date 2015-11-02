/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encore;

import java.util.ArrayList;

/**
 *
 * @author Luca
 */
public class OperacoesComAutomatos {

    public static Automato concatenarAutomatos(Automato a1, Automato a2) {
        
        Automato concatenado;
        
        int indice = a1.getEstados().size();
        
        ArrayList<Estado> novos = new ArrayList<Estado>();
        
        Estado inicial = null;
        
        for(Estado e : a2.getEstados()){
            if(e.getInicial()){
                inicial = e;
                e.setInicial(false);
            }
            e.rename("q" + indice);
            indice++;
        }
        
        for(Estado e : a1.getEstados()){
            if(e.getFinal()){
                e.addTransicaoPorIndice(inicial, 0);
                e.setFinal(false);
            }
            novos.add(e);
        }
        
        for(Estado e : a2.getEstados()){

            novos.add(e);
        }

        concatenado = new Automato(novos, a1.getAlfabeto());
        return concatenado;
        
        
    }
    
    public static Automato UniaoMinimizacaoDeAutomatos(Automato[] automatos) {
        Automato unido = new Automato();
        
        unido.setAlfabeto(automatos[0].getAlfabeto());                                  //como todos os automatos supostamente terão o mesmo alfabeto pode
        
        Estado novoComeco = new Estado(unido, "q0", automatos[0].getAlfabeto().length);
        
        novoComeco.setInicial(true);
        
        int counter = 1;

        unido.addEstados(novoComeco);

        for (Automato a : automatos) {
            Determinizador.Determinizar(a);

            //minimizar(a);
            for (Estado e : a.getEstados()) {

                e.rename("q" + counter);
                counter++;

                if (e.getInicial()) {
                    novoComeco.addTransicaoPorIndice(e, 0);
                    e.setInicial(false);
                }

                unido.addEstados(e);
            }
        }

        return unido;
    }

    public static ArrayList<Estado> RetirarEstadosInalcancaveis(Automato automato, Estado inicio, ArrayList<Estado> alcancaveis) {

        if (alcancaveis.contains(inicio)) {
            return alcancaveis;
        }

        alcancaveis.add(inicio);

        for (int i = 0; i < inicio.getTransicoes().length; i++) {
            for (Estado e : inicio.getTransicaoPorIndice(i)) {
                RetirarEstadosInalcancaveis(automato, e, alcancaveis);
            }
        }

        return alcancaveis;

    }

    //boas praticas...: um método não deve alterar diretamente as entradas, deve retornar algo para ser utilizado depois
    public static Automato criarAutomatoTotal(Automato automato) {
        //Em um automato total, todos os estados possuem transicoes com todos os símbolos
        Automato temp = automato;
        Estado D = new Estado(temp, "q" + temp.getEstados().size(), temp.getAlfabeto().length);
        for (Estado e : temp.getEstados()) {
            for (int i = 1; i < e.getTransicoes().length; i++) {
                if (e.getTransicaoPorIndice(i).size() == 0) {
                    e.addTransicaoPorIndice(D, i);
                }
            }
        }
        for (String s : temp.getAlfabeto()) {
            if (!s.equals("E")) {
                D.addTransicaoPorAlfa(D, s);
            }
        }
        temp.addEstados(D);

        return temp;
    }

    public static Automato MinimizarAutomato(Automato automato) {
        //para minimizar, um automato deve ser deterministico, sem estados inalcansaveis, e total e ja esta tudo feito
        
        
        
        return automato;
    }

    //public static void RetirarEstadosInalcansaveis(Gramatica gramatica){
    //Trabalho 3
    //}
}
