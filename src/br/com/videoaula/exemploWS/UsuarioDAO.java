package br.com.videoaula.exemploWS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UsuarioDAO {

	public int inserirUsuario(Usuario user){
		try {
			Connection conn = ConectaMysql.obtemConexao();

			String queryInserir = "INSERT INTO usuario VALUES (null,?,?,?)";
			PreparedStatement ppstm = conn.prepareStatement(queryInserir);
			
			ppstm.setString(1, user.getNome());
			ppstm.setInt(2, user.getIdade());
			ppstm.setBytes(3, user.getFoto());

			int linhasAfetadas = ppstm.executeUpdate();

			if(linhasAfetadas == 0){
				return -1;
			}
			
			try(ResultSet chaveGerada = ppstm.getGeneratedKeys()){
				if(chaveGerada.next()){
					return (int) chaveGerada.getLong(1);
				}
				else{
					return -1;
				}
			}catch(Exception e){
				e.printStackTrace();
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean excluirUsuario(Usuario user){
		try {
			Connection conn = ConectaMysql.obtemConexao();

			String queryExcluir = "DELETE FROM usuario where id = ?";
			PreparedStatement ppstm = conn.prepareStatement(queryExcluir);

			ppstm.setInt(1, user.getId());

			ppstm.executeUpdate();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	public ArrayList<Usuario> buscarTodosUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();

		try {
			Connection conn = ConectaMysql.obtemConexao();

			String queryBuscarTodosUsuarios = "SELECT * FROM usuario";
			PreparedStatement ppstm = conn.prepareStatement(queryBuscarTodosUsuarios);

			ResultSet rSet = ppstm.executeQuery();

			while(rSet.next()){
				Usuario user = new Usuario();
				
				user.setId(rSet.getInt(1));
				user.setNome(rSet.getString(2));
				user.setIdade(rSet.getInt(3));
				user.setFoto(rSet.getBytes(4));

				lista.add(user);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	public Usuario buscarUsuarioPorID(int id){
		Usuario user = null;

		try {
			Connection conn = ConectaMysql.obtemConexao();

			String queryBuscarUsuarioID = "SELECT * FROM usuario WHERE id = ?";
			PreparedStatement ppstm = conn.prepareStatement(queryBuscarUsuarioID);

			ppstm.setInt(1, id);

			ResultSet rSet = ppstm.executeQuery();

			if(rSet.next()){
				user = new Usuario();

				user.setId(rSet.getInt(1));
				user.setNome(rSet.getString(2));
				user.setIdade(rSet.getInt(3));
				user.setFoto(rSet.getBytes(4));

			}else{
				return user;
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}
	
	public boolean atualizarUsuario(Usuario user){
		try {
			Connection conn = ConectaMysql.obtemConexao();

			String queryInserir = "UPDATE usuario SET nome = ?, idade = ? WHERE id = ?";
			PreparedStatement ppstm = conn.prepareStatement(queryInserir);
			ppstm.setString(1, user.getNome());
			ppstm.setInt(2, user.getIdade());
			ppstm.setInt(3, user.getId());

			ppstm.executeUpdate();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean excluirUsuarioPorID(int id){

		return excluirUsuario(new Usuario(id, "", 0));
	}
}
