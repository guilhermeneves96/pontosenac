package com.pontosenac.pontosenac.controllers;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.pontosenac.pontosenac.componentes.Data;
import com.pontosenac.pontosenac.componentes.Hora;
import com.pontosenac.pontosenac.model.Pessoa;
import com.pontosenac.pontosenac.model.RegistroPonto;
import com.pontosenac.pontosenac.repository.PessoaRepository;
import com.pontosenac.pontosenac.repository.RegistroPontoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/baterponto")
public class BaterpontoController {

    @Autowired
    RegistroPontoRepository registroPontoRepository;
    @Autowired
    PessoaRepository pessoaRepository;

    @GetMapping
    public ModelAndView pagina(Model model, HttpSession session) {

        ModelAndView mv = new ModelAndView("baterponto");
        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");
        Data dataAtual = new Data();
        if (pessoa != null) {
            // AQUI INSERI AS INFORMAÇÕES DE USUARIO E DATA NA PAGINA
            model.addAttribute("pessoa", pessoa);
            model.addAttribute("dataAtual", dataAtual.dataAtual());
            // AQUI CRIA A LISTA DE REGISTRO DE PONTO
            List<RegistroPonto> registrosPonto = registroPontoRepository.findByPessoaId(pessoa.getId());
            registrosPonto.sort(Comparator.comparing(RegistroPonto::getData).reversed());
            mv.addObject("registrosPontos", registrosPonto);
        } else {
            // Se não houver pessoa na sessão, redirecionar para login
            model.addAttribute("erro", "Você não esta Autenticado");
            mv.setViewName("redirect:/login");
        }
        return mv;
    }

    @PostMapping
    public String baterPonto(Model model, HttpSession session) {

        Pessoa pessoa = (Pessoa) session.getAttribute("pessoaAutenticada");

        if (pessoa == null) {
            
            return "redirect:/login"; 
        }

        pessoa = pessoaRepository.findById(pessoa.getId()).orElse(null);
        if (pessoa == null) {
 
            return "redirect:/login"; // ou outra página apropriada
        }

        Data data = new Data();
        Hora hora = new Hora();
        String dataHoje = data.dataAtual();
        String horaAgora = hora.horaAtual();

        List<RegistroPonto> registroEncontrado = registroPontoRepository.findByPessoaAndData(pessoa, dataHoje);

        // Verifica se há registros encontrados
        if (registroEncontrado.isEmpty()) {
            // Criar novo registro
            System.out.println("Novo Registro...");
            RegistroPonto novoRegistro = new RegistroPonto();
            novoRegistro.setPessoa(pessoa);
            novoRegistro.setData(dataHoje);
            novoRegistro.setHoraEntrada(horaAgora);
            novoRegistro.setPeriodo(hora.definirPeriodo(horaAgora));
            novoRegistro.setHoraSaida(null);
            registroPontoRepository.save(novoRegistro);

        } else {
            System.out.println("Verificando registro existente...");
            boolean registroAtualizado = false;

            for (RegistroPonto registroPonto : registroEncontrado) {
                // Verifica se o período do registro existente é diferente do período atual
                if (registroPonto.getPeriodo() != hora.definirPeriodo(horaAgora)) {
                    // Cria um novo registro para um período diferente
                    RegistroPonto novoRegistro = new RegistroPonto();
                    novoRegistro.setPessoa(pessoa);
                    novoRegistro.setData(dataHoje);
                    novoRegistro.setHoraEntrada(horaAgora);
                    novoRegistro.setPeriodo(hora.definirPeriodo(horaAgora));
                    novoRegistro.setHoraSaida(null);
                    registroPontoRepository.save(novoRegistro);
                    registroAtualizado = true;

                } else if (registroPonto.getPeriodo() == hora.definirPeriodo(horaAgora)
                        && !registroPonto.getHoraEntrada().isEmpty()
                        && (registroPonto.getHoraSaida() == null || registroPonto.getHoraSaida().isEmpty())) {
                    // Atualiza a hora de saída
                    registroPonto.setHoraSaida(horaAgora);
                    registroPontoRepository.save(registroPonto);
                    registroAtualizado = true;
                }
            }

            // Se nenhum registro foi atualizado, você pode adicionar uma lógica de erro ou
            // mensagem
            if (!registroAtualizado) {
                System.out.println("Nenhum registro foi atualizado.");
            }
        }

        return "redirect:/baterponto";
    }

}