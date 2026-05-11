from pathlib import Path
from PIL import Image
import hashlib
from collections import defaultdict

root = Path(r"D:\Games\Minecraft\Coding\Tech-Ascension-Workspace\TechCore\src\main\resources\assets\techcore\textures\item\material_icons")

def texture_hash(path):
    with Image.open(path) as img:
        img = img.convert("RGBA")
        size = img.size
        data = img.tobytes()
        return hashlib.sha256(
            size[0].to_bytes(4, "big") +
            size[1].to_bytes(4, "big") +
            data
        ).hexdigest()

texture_map = defaultdict(list)

for subfolder in root.iterdir():
    if subfolder.is_dir():
        for file in subfolder.rglob("*.png"):
            try:
                h = texture_hash(file)
                texture_map[h].append((subfolder.name, file))
            except Exception as e:
                print(f"Skipped {file}: {e}")

folder_groups = defaultdict(list)

for h, items in texture_map.items():
    folders = tuple(sorted(set(folder for folder, _ in items)))

    if len(folders) > 1:
        folder_groups[folders].append(items)

output_file = root / "texture_report.txt"

with open(output_file, "w", encoding="utf-8") as f:
    f.write("=== Folder Matches ===\n\n")

    if not folder_groups:
        f.write("No matching textures found.\n")
    else:
        for group, textures in sorted(folder_groups.items()):
            all_texture_names = set()

            for tex in textures:
                for _, file in tex:
                    all_texture_names.add(file.name)

            f.write(f"{', '.join(group)} share {len(all_texture_names)} texture name(s)\n\n")

            for texture_name in sorted(all_texture_names):
                f.write(f"  {texture_name}\n")

            f.write("\n" + "-" * 60 + "\n\n")

print(f"Report written to: {output_file}")