package com.barisgungorr.rickandmortyapp.util.extension

class Url {
    companion object {
        fun obtainNumFromURLs(urls: List<String>): String {
            val num = mutableListOf<String>()

            for (url in urls) {
                val patron = "\\d+$" // Busca uno o más dígitos al final de la cadena
                val regex = Regex(patron)
                val coincidencias = regex.findAll(url)
                val listaNumeros = coincidencias.map { it.value }.toList()

                if (listaNumeros.isNotEmpty()) {
                    num.addAll(listaNumeros)
                }
            }

            if (num.size > 1) {
                val ultimoNumero = num.last()
                num[num.size - 1] = ultimoNumero.substringBeforeLast(",")
            }

            return num.joinToString(", ")
        }

    }
}