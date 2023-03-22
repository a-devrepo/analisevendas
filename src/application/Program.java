package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Sale;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Locale.setDefault(Locale.US);

		System.out.println("Entre o caminho do arquivo: ");
		String path = sc.nextLine();

		Set<Sale> salesList = new HashSet<Sale>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();

			while (line != null) {
				String[] fields = line.split(",");
				Integer month = Integer.parseInt(fields[0]);
				Integer year = Integer.parseInt(fields[1]);
				String seller = fields[2];
				Integer items = Integer.parseInt(fields[3]);
				Double total = Double.parseDouble(fields[4]);
				salesList.add(new Sale(month, year, seller, items, total));
				line = br.readLine();
			}

			Predicate<Sale> predYear = (s) -> s.getYear().intValue() == 2016;
			Comparator<Sale> comp = (s1, s2) -> s1.averagePrice().compareTo(s2.averagePrice());

			List<Sale> collected = salesList.stream().sorted(comp.reversed()).filter(predYear).limit(5)
					.collect(Collectors.toList());

			Predicate<Sale> predSeller = (s) -> s.getSeller().equalsIgnoreCase("Logan");
			Predicate<Sale> predMonth = (s) -> {
				return s.getMonth().intValue() == 1 || s.getMonth().intValue() == 7;
			};

			Double loganTotal = salesList.stream().filter(predSeller).filter(predMonth).map(s -> s.getTotal())
					.reduce(0.0, (x, y) -> x + y);

			System.out.printf("%nCinco primeiras vendas de 2016 de maior preço médio");
			for (Sale sale : collected) {
				System.out.printf("%n%s", sale);
			}

			System.out.printf("%n%nValor total vendido pelo vendedor Logan nos meses 1 e 7 = %.2f", loganTotal);

		} catch (IOException e) {
			System.out.printf("Erro: %s (O sistema não pode encontrar o arquivo especificado)", path);
		} catch (NumberFormatException e) {
			System.out.printf("Erro: %s", e.getMessage());
		} finally {
			sc.close();
		}
	}
}
