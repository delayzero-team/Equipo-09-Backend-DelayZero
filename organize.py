import os
import shutil

# --- Configuración ---
frontend_dir_original = "frontEndFlightOnTime"
frontend_dir_nuevo = "frontend"
backend_dir = "backend"
script_nombre = os.path.basename(__file__)
powershell_script = "organize.ps1"
# --- Fin de la Configuración ---

def organizar_proyecto():
    """
    Organiza el proyecto separando los archivos de frontend y backend.
    Este script es idempotente y se puede ejecutar varias veces de forma segura.
    """
    print("Iniciando la organización del proyecto...")
    
    # --- Parte 1: Renombrar el directorio frontend ---
    if os.path.isdir(frontend_dir_original):
        shutil.move(frontend_dir_original, frontend_dir_nuevo)
        print(f"Directorio '{frontend_dir_original}' renombrado a '{frontend_dir_nuevo}'.")
    
    # --- Parte 2: Mover los archivos del backend ---
    if not os.path.isdir(backend_dir):
        os.makedirs(backend_dir)
        print(f"Directorio '{backend_dir}' creado.")

    # Lista todos los items en el directorio raíz
    try:
        items_raiz = os.listdir('.')
    except FileNotFoundError:
        print("Error: El directorio actual no existe.")
        return
        
    # Define los elementos que NO deben moverse
    elementos_protegidos = {frontend_dir_nuevo, backend_dir, script_nombre, powershell_script}

    # Identifica los items a mover al directorio 'backend'
    items_a_mover = [item for item in items_raiz if item not in elementos_protegidos]

    if not items_a_mover:
        print("Todos los archivos del backend ya parecen estar en su directorio.")
    else:
        print(f"Se moverán {len(items_a_mover)} elemento(s) a '{backend_dir}':")
        for item in items_a_mover:
            try:
                # Mueve el item a la carpeta backend
                origen = os.path.join('.', item)
                destino = os.path.join(backend_dir, item)
                shutil.move(origen, destino)
                print(f"- Moviendo '{item}'")
            except Exception as e:
                print(f"- No se pudo mover '{item}'. Razón: {e}")

    print("\nOrganización del proyecto completada.")

if __name__ == "__main__":
    organizar_proyecto()
